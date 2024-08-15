package com.goal.domain.strategy.service.rule.chain;

/**
 * 抽奖前置规则过滤
 *      如：默认、黑名单、权重
 */
public abstract class AbstractLogicChain implements ILogicChain {

    private ILogicChain next;

    @Override
    public ILogicChain next() {
        return next;
    }

    @Override
    public ILogicChain appendNext(ILogicChain next) {
        this.next = next;
        return next;
    }

    abstract protected String ruleModel();
}
