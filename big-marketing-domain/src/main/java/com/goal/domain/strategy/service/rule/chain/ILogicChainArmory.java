package com.goal.domain.strategy.service.rule.chain;

/**
 * 责任链模式
 *      定义责任链的方法
 */
public interface ILogicChainArmory {

    ILogicChain next();

    ILogicChain appendNext(ILogicChain next);

}
