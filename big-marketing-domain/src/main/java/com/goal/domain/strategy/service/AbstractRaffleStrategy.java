package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.goal.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.goal.types.enums.ResponseCode;
import com.goal.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

/**
 * 规则过滤抽奖的抽象模板<br/>
 * 1. 校验参数<br/>
 * 2. 查询策略所用规则<br/>
 * 3. 根据规则进行过滤<br/>
 * 4. 根据过滤结果，决定如何进行抽奖<br/>
 * 黑名单：奖品信息已知  [105:user001,user002] 奖品id是：105<br/>
 * 权重：根据key调用之前的方法<br/>
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    @Resource
    private IStrategyRepository repository;

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor) {
        // 1. 参数校验
        String userId = raffleFactor.getUserId();
        Long strategyId = raffleFactor.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 责任链抽奖计算【这步拿到的是初步的抽奖ID，之后需要根据ID处理抽奖】注意；黑名单、权重等非默认抽奖的直接返回抽奖结果
        DefaultChainFactory.StrategyAwardVO chainStrategyAwardVO = raffleLogicChain(userId, strategyId);

        Integer awardId = chainStrategyAwardVO.getAwardId();
        log.info("抽奖策略计算-责任链 {} {} {} {}", userId, strategyId, awardId, chainStrategyAwardVO.getLogicModel());
        if (!DefaultChainFactory.LogicModel.RULE_DEFAULT.getCode().equals(chainStrategyAwardVO.getLogicModel())) {
            // TODO: 2024/8/20 补充awardConfig
            return buildRaffleAwardEntity(strategyId, awardId, null);
        }

        // 3. 规则树抽奖过滤【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
        DefaultTreeFactory.StrategyAwardVO treeStrategyAwardVO = raffleLogicTree(userId, strategyId,
                awardId);
        log.info("抽奖策略计算-规则树 {} {} {} {}", userId, strategyId, treeStrategyAwardVO.getAwardId(), treeStrategyAwardVO.getAwardRuleValue());

        // 4. 返回抽奖结果
        return buildRaffleAwardEntity(strategyId, awardId, treeStrategyAwardVO.getAwardRuleValue());

    }

    private RaffleAwardEntity buildRaffleAwardEntity(Long strategyId, Integer awardId, String awardConfig) {
        StrategyAwardEntity strategyAwardEntity = repository.queryStrategyAwardEntity(strategyId, awardId);
        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .awardConfig(awardConfig)
                .sort(strategyAwardEntity.getSort())
                .build();
    }

    /**
     * 抽奖前规则过滤，责任链
     * @param userId 用户ID
     * @param strategyId 策略ID
     * @return 奖品ID
     */
    protected abstract DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId);

    /**
     * 抽奖中计算，决策树
     *
     * @param userId     用户ID
     * @param strategyId 策略ID
     * @param awardId    奖品ID
     * @return 过滤结果【奖品ID，会根据抽奖次数判断、库存判断、兜底兜里返回最终的可获得奖品信息】
     */
    protected abstract DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId,
                                                                          Long strategyId, Integer awardId);
}
