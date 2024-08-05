package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;

public interface IRaffleStrategy {

    /**
     * 包含规则过滤的抽奖
     * @param raffleFactor 因子
     * @return 抽奖所获得的奖品信息
     */
    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor);

}
