package com.goal.infrastructure.persistent.dao;

import com.goal.infrastructure.persistent.po.StrategyAward;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface IStrategyAwardDao {
    List<StrategyAward> queryAll();

    List<StrategyAward> queryByStrategyId(Long strategyId);
}
