package com.goal.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 决策树节点连线
 *  标识一个节点怎么到下一个节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeNodeLineVO {

    /**
     * 规则树ID
     */
    private String treeId;

    /**
     * 规则 key 节点：FROM
     */
    private String ruleNodeFrom;

    /**
     * 规则 key 节点：TO
     */
    private String ruleNodeTo;

    /**
     * 限定规则类型
     */
    private RuleLimitTypeVO ruleLimitType;

    /**
     * 限定值 到下个节点
     */
    private RuleLogicCheckTypeVO ruleLimitValue;

}
