package com.goal.domain.strategy.service.armory;

/**
 * 策略装配工厂
 */
public interface IStrategyArmory {

    void assembleLotteryStrategy(Long strategyId);

    Integer getRandomAwardId(Long strategyId);

}
