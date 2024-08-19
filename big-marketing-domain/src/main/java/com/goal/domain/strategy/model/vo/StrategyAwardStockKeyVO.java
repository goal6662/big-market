package com.goal.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 策略奖品库存 key 标识值对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StrategyAwardStockKeyVO {

    /**
     * 奖品ID
     */
    private Integer awardId;

    /**
     * 策略ID
     */
    private Long strategyId;

}
