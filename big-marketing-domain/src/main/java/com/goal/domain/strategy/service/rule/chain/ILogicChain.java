package com.goal.domain.strategy.service.rule.chain;

import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;

/**
 * 定义责任链的各个节点所要实现的方法
 */
public interface ILogicChain extends ILogicChainArmory, Cloneable {

    /**
     * 责任链接口
     *      责任链完成后需要返回具体的奖品ID
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId);

}
