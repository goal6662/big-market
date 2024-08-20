package com.goal.domain.strategy.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 抽奖获取到的具体奖品
 *  属性完全来自于Award类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RaffleAwardEntity {

    /**
     * 奖品ID
     */
    private Integer awardId;

    /**
     * 奖品配置信息
     */
    private String awardConfig;

    /**
     * 排序规则
     */
    private Integer sort;

}
