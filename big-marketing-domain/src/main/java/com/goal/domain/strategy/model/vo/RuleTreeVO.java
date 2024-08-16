package com.goal.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 决策树的树根信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeVO {

    /**
     * 规则树ID
     */
    private String treeId;

    /**
     * 决策树名称
     */
    private String treeName;

    /**
     * 决策树描述
     */
    private String treeDesc;

    /**
     * 决策树根节点
     */
    private String treeRootRuleNode;

    /**
     * 规则节点
     */
    private Map<String, RuleTreeNodeVO> treeNodeMap;

}
