package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryAllRecord();

}