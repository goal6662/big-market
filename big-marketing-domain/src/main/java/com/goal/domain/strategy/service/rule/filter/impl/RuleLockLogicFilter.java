package com.goal.domain.strategy.service.rule.filter.impl;

import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.entity.RuleMatterEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.annotation.LogicStrategy;
import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * 当抽中的奖品需要次数才能解锁时的处理逻辑
 *  次数满足，返回抽取到的奖品
 *  不满足，返回兜底奖励
 */
@Slf4j
@LogicStrategy(logicModel = DefaultLogicFactory.LogicModel.RULE_LOCK)
public class RuleLockLogicFilter implements ILogicFilter<RuleActionEntity.RaffleCenterEntity> {

    @Resource
    private IStrategyRepository repository;

    private Long userRaffleCount = 10L;

    @Override
    public RuleActionEntity<RuleActionEntity.RaffleCenterEntity> filter(RuleMatterEntity ruleMatter) {

        log.info("规则过滤-兜底奖励 userId:{} strategyId:{} ruleModel:{}",
                ruleMatter.getUserId(), ruleMatter.getStrategyId(), ruleMatter.getRuleModel());

        String ruleValue = repository.queryStrategyRuleValue(ruleMatter.getStrategyId(),
                ruleMatter.getAwardId(), ruleMatter.getRuleModel());

        // 需要的抽奖次数
        long needCount = Long.parseLong(ruleValue);
        if (userRaffleCount >= needCount) {
            // 满足条件，放行
            return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                    .code(RuleLogicCheckTypeVO.ALLOW.getCode())
                    .info(RuleLogicCheckTypeVO.ALLOW.getInfo())
                    .build();
        }

        // 不满足，执行拦截
        // 过滤器只负责是否接管，后续流程由其他业务处理
        return RuleActionEntity.<RuleActionEntity.RaffleCenterEntity>builder()
                .code(RuleLogicCheckTypeVO.TAKE_OVER.getCode())
                .info(RuleLogicCheckTypeVO.TAKE_OVER.getInfo())
                .build();
    }

}
