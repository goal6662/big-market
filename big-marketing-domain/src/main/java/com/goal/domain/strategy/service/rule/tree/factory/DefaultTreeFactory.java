package com.goal.domain.strategy.service.rule.tree.factory;

import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.model.vo.RuleTreeVO;
import com.goal.domain.strategy.service.rule.tree.ILogicTreeNode;
import com.goal.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import com.goal.domain.strategy.service.rule.tree.factory.engine.impl.DecisionTreeEngine;
import lombok.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class DefaultTreeFactory {

    private final Map<String, ILogicTreeNode> logicTreeNodeGroup;

    public IDecisionTreeEngine openLogicTree(RuleTreeVO ruleTreeVO) {
        return new DecisionTreeEngine(logicTreeNodeGroup, ruleTreeVO);
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TreeActionEntity {
        /**
         * 校验结果
         */
        private RuleLogicCheckTypeVO ruleLogicCheckType;

        private StrategyAwardVO strategyAwardData;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StrategyAwardVO {
        private Integer awardId;
        private String awardRuleValue;
    }
}
