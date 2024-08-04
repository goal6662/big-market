package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.Strategy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface IStrategyDao {
    List<Strategy> queryAll();

    Strategy queryByStrategyId(@Param("strategyId") Long strategyId);
}
