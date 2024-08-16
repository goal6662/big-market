package com.goal.domain.strategy.service.rule.tree.impl;

import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.goal.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import org.springframework.stereotype.Component;

@Component("rule_lock")
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    /**
     * 放行？
     */
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {
        return DefaultTreeFactory
                .TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                .build();
    }

}
