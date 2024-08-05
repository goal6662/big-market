package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;

public interface IRaffleStrategy {

    RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor);

}
