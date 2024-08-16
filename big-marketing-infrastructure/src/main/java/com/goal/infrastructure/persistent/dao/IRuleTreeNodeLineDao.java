package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.RuleTreeNodeLine;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IRuleTreeNodeLineDao {
    List<RuleTreeNodeLine> queryRuleTreeNodeLineListByTreeId(@Param("treeId") String treeId);
}
