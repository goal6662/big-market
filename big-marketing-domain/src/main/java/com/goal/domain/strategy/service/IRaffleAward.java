package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;

import java.util.List;

public interface IRaffleAward {

    List<StrategyAwardEntity> queryRaffleStrategyAwardList(Long strategyId);

}
