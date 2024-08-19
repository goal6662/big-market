package com.goal.domain.strategy.service.armory;

public interface IStrategyDispatch {

    Integer getRandomAwardId(Long strategyId);

    Integer getRandomAwardId(Long strategyId, String ruleWeightValue);

    /**
     * 扣减商品库存
     * @return 是否成功
     */
    Boolean subAwardStock(Long strategyId, Integer awardId);
}
