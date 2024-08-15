package com.goal.domain.strategy.service.rule.filter.impl;

import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.entity.RuleMatterEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.annotation.LogicStrategy;
import com.goal.domain.strategy.service.rule.filter.ILogicFilter;
import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.types.common.Constants;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.RULE_BLACKLIST)
public class RuleBlackListLogicFilter implements ILogicFilter<RuleActionEntity.RaffleBeforeEntity> {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> filter(RuleMatterEntity ruleMatter) {

        log.info("规则过滤-黑名单 userId:{} strategyId:{}, ruleModel:{}",
                ruleMatter.getUserId(), ruleMatter.getStrategyId(), ruleMatter.getRuleModel());

        String userId = ruleMatter.getUserId();

        // 查询策略规则配置
        String ruleValue = repository.queryStrategyRuleValue(ruleMatter.getStrategyId(),
                ruleMatter.getAwardId(), ruleMatter.getRuleModel());

        String[] splitRuleValue = ruleValue.split(Constants.COLON);

        int awardId = Integer.parseInt(splitRuleValue[0]);

        String[] blackUserIdList = splitRuleValue[1].split(Constants.SPLIT);
        for (String blackUserId : blackUserIdList) {

            // 当前用户位于黑名单
            if (blackUserId.equals(userId)) {
                // 规则引擎接管后续操作
                return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                        .ruleModel(DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode())
                        .data(
                                RuleActionEntity.RaffleBeforeEntity.builder()
                                        .strategyId(ruleMatter.getStrategyId())
                                        .awardId(awardId)
                                        .build()
                        )
                        .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                        .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                        .build();
            }
        }

        return RuleActionEntity.<RuleActionEntity.RaffleBeforeEntity>builder()
                .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                .build();
    }
}
