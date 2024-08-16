package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.RuleTree;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IRuleTreeDao {
    RuleTree queryRuleTreeByTreeId(@Param("treeId") String treeId);
}
