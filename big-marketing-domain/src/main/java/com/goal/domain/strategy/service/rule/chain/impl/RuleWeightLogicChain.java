package com.goal.domain.strategy.service.rule.chain.impl;

import com.goal.domain.strategy.model.entity.StrategyRuleEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import com.goal.domain.strategy.service.rule.chain.AbstractLogicChain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component("rule_weight")
@RequiredArgsConstructor
public class RuleWeightLogicChain extends AbstractLogicChain {

    private final IStrategyRepository repository;

    private final IStrategyDispatch dispatch;

    public Long userScore = 0L;

    @Override
    protected String ruleModel() {
        return "rule_weight";
    }

    @Override
    public Integer logic(String userId, Long strategyId) {
        log.info("抽奖责任链-权重开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleModel());
        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();

        if (ruleWeightValues != null) {
            // 找到满足的最大权重
            List<Long> sortedWeightList = ruleWeightValues.keySet().stream()
                    .map(Long::valueOf)
                    .sorted()
                    .collect(Collectors.toList());

            String ruleWeightValueKey;
            for (int i = sortedWeightList.size() - 1; i >= 0; i--) {
                if (sortedWeightList.get(i) <= userScore) {
                    ruleWeightValueKey = String.valueOf(sortedWeightList.get(i));
                    // 找到了满足的最小权重
                    return dispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                }
            }
        }

        log.info("抽奖责任链-权重放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        return next().logic(userId, strategyId);
    }
}
