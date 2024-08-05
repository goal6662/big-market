package com.goal.domain.strategy.repository;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.model.entity.StrategyRuleEntity;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    void storeStrategyAwardSearchTables(String key, int rateRange, Map<Integer, Integer> shuffledAwardSearchRateTables);

    int getRateRange(Long strategyId);

    int getRateRange(String key);

    Integer getStrategyAwardAssemble(Long strategyId, int rateIndex);

    Integer getStrategyAwardAssemble(String key, int rateIndex);

    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);
}
