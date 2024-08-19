package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.vo.StrategyAwardStockKeyVO;

/**
 * 操作商品库存
 */
public interface IRaffleStock {

    /**
     * 获取奖品库存消费队列
     *
     * @return 奖品库存 Key 信息
     * @throws InterruptedException 异常
     */
    StrategyAwardStockKeyVO takeQueueValue() throws InterruptedException;

    /**
     * 数据库更新奖品库存
     *
     * @param strategyId 策略ID
     * @param awardId 奖品ID
     */
    void updateStrategyAwardStock(Long strategyId, Integer awardId);

}
