package com.goal.trigger.http;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.service.IRaffleAward;
import com.goal.domain.strategy.service.IRaffleStrategy;
import com.goal.domain.strategy.service.armory.IStrategyArmory;
import com.goal.trigger.api.IRaffleService;
import com.goal.trigger.api.dto.RaffleAwardListRequestDTO;
import com.goal.trigger.api.dto.RaffleAwardListResponseDTO;
import com.goal.trigger.api.dto.RaffleRequestDTO;
import com.goal.trigger.api.dto.RaffleResponseDTO;
import com.goal.types.enums.ResponseCode;
import com.goal.types.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 抽奖服务
 */
@Slf4j
@CrossOrigin(origins = "*") //表示只允许这一个url可以跨域访问这个controller
@RestController
@RequestMapping("/api/${app.config.api-version}/raffle")
public class IRaffleController implements IRaffleService {

    @Resource
    private IStrategyArmory strategyArmory;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Resource
    private IRaffleAward raffleAward;

    @Override
    @GetMapping("strategy_armory")
    public Response<Boolean> strategyArmory(Long strategyId) {

        if (strategyId == null) {
            return Response.fail(ResponseCode.ILLEGAL_PARAMETER);
        }

        try {

            log.info("抽奖策略装配开始 strategyId: {}", strategyId);
            boolean armoryStatus = strategyArmory.assembleLotteryStrategy(strategyId);
            log.info("抽奖策略装配完成 strategyId: {}", strategyId);

            return Response.success(armoryStatus);
        } catch (Exception e) {
            log.info("抽奖策略装配失败 strategyId: {}", strategyId, e);
            return Response.fail();
        }

    }

    @Override
    @PostMapping("query_raffle_award_list")
    public Response<List<RaffleAwardListResponseDTO>> queryRaffleAwardList(
            @RequestBody RaffleAwardListRequestDTO reqDTO) {

        try {
            log.info("查询奖品列表开始 strategyId: {}", reqDTO.getStrategyId());
            List<StrategyAwardEntity> strategyAwardEntityList =
                    raffleAward.queryRaffleStrategyAwardList(reqDTO.getStrategyId());

            List<RaffleAwardListResponseDTO> responseDTOList = strategyAwardEntityList.stream().map(item -> {
                RaffleAwardListResponseDTO responseDTO = new RaffleAwardListResponseDTO();

                BeanUtils.copyProperties(item, responseDTO);
                return responseDTO;
            }).collect(Collectors.toList());

            return Response.success(responseDTOList);

        } catch (Exception e) {
            log.info("查询奖品列表失败 strategyId: {}", reqDTO.getStrategyId(), e);
            return Response.fail();
        }

    }

    @Override
    @PostMapping("random_raffle")
    public Response<RaffleResponseDTO> performRaffle(@RequestBody RaffleRequestDTO reqDTO) {

        try {
            log.info("随机抽奖开始 strategyId: {}", reqDTO.getStrategyId());
            RaffleAwardEntity awardEntity = raffleStrategy.performRaffle(RaffleFactorEntity.builder()
                    .strategyId(reqDTO.getStrategyId())
                    .userId("system")
                    .build());

            return Response.success(RaffleResponseDTO.builder()
                            .awardId(awardEntity.getAwardId())
                            .awardIndex(awardEntity.getSort())
                    .build());

        } catch (Exception e) {
            log.error("随机抽奖失败 strategyId: {}", reqDTO.getStrategyId(), e);
            return Response.fail();
        }
        
    }
}
