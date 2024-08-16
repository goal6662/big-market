package com.goal.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 决策树的节点
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RuleTreeNodeVO {

    /**
     * 规则树ID
     */
    private String treeId;

    /**
     * 规则 key
     */
    private String ruleKey;

    /**
     * 规则描述
     */
    private String ruleDesc;

    /**
     * 规则比值
     */
    private String ruleValue;

    /**
     * 规则连线
     */
    private List<RuleTreeNodeLineVO> treeNodeLineVOList;

}
