package com.goal.domain.strategy.service.rule.filter.impl;

import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.entity.RuleMatterEntity;
import com.goal.domain.strategy.model.entity.StrategyRuleEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.annotation.LogicStrategy;
import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.RULE_WEIGHT)
public class RuleWeightLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    private final String userWeight = "1000";

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatter) {
        log.info("规则过滤-权重范围 userId:{} strategyId:{} ruleModel:{}",
                ruleMatter.getUserId(), ruleMatter.getStrategyId(), ruleMatter.getRuleModel());

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(ruleMatter.getStrategyId(), ruleMatter.getRuleModel());
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        if (ruleWeightValues == null) {
            // 不是权重规则，不需要进行接管
            return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        // 找到满足的最大权重
        List<Long> sortedWeightList = ruleWeightValues.keySet().stream()
                .map(Long::valueOf)
                .sorted()
                .collect(Collectors.toList());

        String ruleWeightValueKey = null;
        for (int i = sortedWeightList.size() - 1; i >= 0; i--) {
            if (sortedWeightList.get(i) <= Long.parseLong(userWeight)) {
                ruleWeightValueKey = String.valueOf(sortedWeightList.get(i));
                // 找到了满足的最小权重
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode())
                        .data(
                                RuleActionEntity.RaffleBeforeEntity.builder()
                                        .strategyId(ruleMatter.getStrategyId())
                                        .ruleWeightValueKey(ruleWeightValueKey)
                                        .build()
                        )
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        // 没有找到，不进行接管
        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
