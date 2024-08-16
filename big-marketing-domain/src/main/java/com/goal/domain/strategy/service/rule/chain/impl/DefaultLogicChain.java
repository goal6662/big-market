package com.goal.domain.strategy.service.rule.chain.impl;

import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import com.goal.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 默认策略，直接进行抽奖即可
 */
@Slf4j
@Component("rule_default")
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DefaultLogicChain extends AbstractLogicChain {

    private final IStrategyDispatch dispatch;

    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        Integer awardId = dispatch.getRandomAwardId(strategyId);

        log.info("抽奖责任链-默认处理 userId: {} strategyId: {} ruleModel: {} awardId: {}",
                userId, strategyId, ruleModel(), awardId);

        return DefaultChainFactory.StrategyAwardVO.builder()
                .awardId(awardId)
                .logicModel(ruleModel())
                .build();
    }

    @Override
    protected String ruleModel() {
        return "rule_default";
    }
}
