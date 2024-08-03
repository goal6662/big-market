package com.goal.domain.strategy.repository;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchTables(Long strategyId, int rateRange, Map<Integer, Integer> shuffledAwardSearchRateTables);

    int getRateRange(Long strategyId);

    Integer getStrategyAwardAssemble(Long strategyId, int rateIndex);
}
