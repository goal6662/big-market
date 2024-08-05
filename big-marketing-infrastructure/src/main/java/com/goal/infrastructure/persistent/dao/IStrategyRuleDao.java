package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.StrategyRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyRuleDao {

    List<StrategyRule> queryAllRecord();

    StrategyRule queryStrategyRule(StrategyRule strategyRuleReq);

    String queryStrategyRuleValue(@Param("strategyId") Long strategyId,
                                  @Param("awardId") Integer awardId,
                                  @Param("ruleModel") String ruleModel);
}
