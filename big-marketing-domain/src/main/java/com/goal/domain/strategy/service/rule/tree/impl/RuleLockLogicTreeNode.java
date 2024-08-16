package com.goal.domain.strategy.service.rule.tree.impl;

import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.goal.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("rule_lock")
@RequiredArgsConstructor
public class RuleLockLogicTreeNode implements ILogicTreeNode {

    public Integer userCount = 9;

    private final IStrategyRepository repository;

    /**
     * 放行？
     */
    @Override
    public DefaultTreeFactory.TreeActionEntity logic(String userId, Long strategyId, Integer awardId) {

        String ruleValue = repository.queryStrategyRuleValue(strategyId, awardId, ruleModel());
        int requiredCount = Integer.parseInt(ruleValue);

        if (userCount >= requiredCount) {
            // 满足最低抽奖次数
            log.info("规则树，次数满足，放行 strategyId:{}, awardId:{}, required:{}, actual:{}",
                    strategyId, awardId, requiredCount, userCount);
            return DefaultTreeFactory
                    .TreeActionEntity.builder()
                    .ruleLogicCheckType(RuleLogicCheckTypeVO.ALLOW)
                    .strategyAwardData(
                            DefaultTreeFactory.StrategyAwardVO.builder()
                                    .awardId(awardId)
                                    .awardRuleValue(ruleValue)
                                    .build()
                    )
                    .build();
        }

        log.info("规则树，次数不满足，接管 strategyId:{}, awardId:{}, required:{}, actual:{}",
                strategyId, awardId, requiredCount, userCount);
        return DefaultTreeFactory
                .TreeActionEntity.builder()
                .ruleLogicCheckType(RuleLogicCheckTypeVO.TAKE_OVER)
                .build();

    }

    private String ruleModel() {
        return "rule_lock";
    }

}
