package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import com.goal.domain.strategy.service.rule.chain.ILogicChain;
import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.goal.types.enums.ResponseCode;
import com.goal.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 规则过滤抽奖的抽象模板<br/>
 *  1. 校验参数<br/>
 *  2. 查询策略所用规则<br/>
 *  3. 根据规则进行过滤<br/>
 *  4. 根据过滤结果，决定如何进行抽奖<br/>
 *      黑名单：奖品信息已知  [105:user001,user002] 奖品id是：105<br/>
 *      权重：根据key调用之前的方法<br/>
 */
@Slf4j
public abstract class AbstractRaffleStrategy implements IRaffleStrategy {

    @Resource
    protected IStrategyRepository repository;

    @Resource
    protected IStrategyDispatch strategyDispatch;

    @Resource
    protected DefaultChainFactory chainFactory;

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor) {
        // 1. 参数校验
        String userId = raffleFactor.getUserId();
        Long strategyId = raffleFactor.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 获取责任链起点
        ILogicChain logicChain = chainFactory.openLogicChain(strategyId);

        // 3. 执行责任链，获取奖品ID
        Integer awardId = logicChain.logic(userId, strategyId);

        // 4. 策略查询
        StrategyAwardRuleModelVO awardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);

        // 5. 抽奖中-规则过滤：根据奖品ID获取用户是否可抽取奖品
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleCenterAction =
                doCheckRaffleCenterAction(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(),
                        awardRuleModelVO.raffleCenterRuleModels());

        if (RuleLogicCheckTypeVO.isTakeOver(ruleCenterAction.getCode())) {
            log.info("【临时日志】中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。");
            return RaffleAwardEntity.builder()
                    .awardDesc("中奖中规则拦截，通过抽奖后规则 rule_luck_award 走兜底奖励。")
                    .build();
        }

        return RaffleAwardEntity.builder()
                .awardId(awardId)
                .build();

    }

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterAction(
            RaffleFactorEntity raffleFactorEntity, List<String> logics
    );
}
