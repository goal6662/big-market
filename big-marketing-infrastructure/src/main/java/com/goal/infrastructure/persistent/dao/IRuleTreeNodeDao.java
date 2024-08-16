package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.RuleTreeNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRuleTreeNodeDao {
    List<RuleTreeNode> queryRuleTreeNodeListByTreeId(@Param("treeId") String treeId);
}
