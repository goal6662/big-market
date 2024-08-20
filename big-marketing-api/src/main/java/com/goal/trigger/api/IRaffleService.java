package com.goal.trigger.api;

import com.goal.trigger.api.dto.RaffleAwardListRequestDTO;
import com.goal.trigger.api.dto.RaffleAwardListResponseDTO;
import com.goal.trigger.api.dto.RaffleRequestDTO;
import com.goal.trigger.api.dto.RaffleResponseDTO;
import com.goal.types.model.Response;

import java.util.List;

/**
 * 抽奖接口
 */
public interface IRaffleService {

    /**
     * 装配策略
     * @param strategyId 策略ID
     * @return 装配结果
     */
    Response<Boolean> strategyArmory(Long strategyId);

    /**
     * 查询奖品列表
     * @param reqDTO 查询参数
     * @return 奖品列表
     */
    Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(RaffleAwardListRequestDTO reqDTO);

    /**
     * 随机抽奖
     * @param reqDTO 请求参数
     * @return  抽奖结果
     */
    Response<RaffleResponseDTO> performRaffle(RaffleRequestDTO reqDTO);

}
