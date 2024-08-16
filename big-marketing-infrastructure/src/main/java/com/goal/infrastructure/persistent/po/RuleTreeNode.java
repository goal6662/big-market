package com.goal.infrastructure.persistent.po;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
public class RuleTreeNode implements Serializable {
    /**
     * 自增ID
     */
    private Long id;

    /**
     * 规则树ID
     */
    private String treeId;

    /**
     * 规则Key
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
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}