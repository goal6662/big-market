package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryAll();

    List<StrategyAward> queryByStrategyId(@Param("strategyId") Long strategyId);

    String queryStrategyAwardRuleModels(StrategyAward strategyAward);

    void updateStrategyAwardStock(@Param("strategyId") Long strategyId, @Param("awardId") Integer awardId);

    StrategyAward queryStrategyAward(@Param("strategyId") Long strategyId, @Param("awardId") Integer awardId);
}
