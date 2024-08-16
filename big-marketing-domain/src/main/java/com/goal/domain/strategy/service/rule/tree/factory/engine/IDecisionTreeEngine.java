package com.goal.domain.strategy.service.rule.tree.factory.engine;

import com.goal.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;

/**
 * 引擎执行
 */
public interface IDecisionTreeEngine {

    DefaultTreeFactory.StrategyAwardData process(String userId, Long strategyId, Integer awardId);

}
