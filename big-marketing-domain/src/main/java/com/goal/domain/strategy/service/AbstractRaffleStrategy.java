package com.goal.domain.strategy.service;

import com.goal.domain.strategy.model.entity.RaffleAwardEntity;
import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.types.enums.ResponseCode;
import com.goal.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;

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

    @Override
    public RaffleAwardEntity performRaffle(RaffleFactorEntity raffleFactor) {
        // 1. 参数校验
        String userId = raffleFactor.getUserId();
        Long strategyId = raffleFactor.getStrategyId();
        if (null == strategyId || StringUtils.isBlank(userId)) {
            throw new AppException(ResponseCode.ILLEGAL_PARAMETER.getCode(), ResponseCode.ILLEGAL_PARAMETER.getInfo());
        }

        // 2. 策略查询
        // 找出当前策略所用到的所有规则，进行过滤
        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);


        // 3. 抽奖前 - 规则过滤
        RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> ruleActionEntity =
                this.doCheckRaffleBeforeLogic(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(),
                        strategy.getRuleModels());

        // 4. 是否接管后续流程
        if (RuleLogicCheckTypeVO.TAKE_OVER.getCode().equals(ruleActionEntity.getCode())) {
            if (DefaultLogicFactory.LogicModel.RULE_BLACKLIST.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 黑名单返回固定的奖品ID
                return RaffleAwardEntity.builder()
                        .awardId(ruleActionEntity.getData().getAwardId())
                        .build();
            } else if (DefaultLogicFactory.LogicModel.RULE_WEIGHT.getCode().equals(ruleActionEntity.getRuleModel())) {
                // 权重根据返回的信息进行抽奖
                RuleActionEntity.RaffleBeforeEntity raffleBeforeEntity = ruleActionEntity.getData();
                String ruleWeightValueKey = raffleBeforeEntity.getRuleWeightValueKey();
                // 进行权重抽奖
                Integer awardId = strategyDispatch.getRandomAwardId(strategyId, ruleWeightValueKey);
                return RaffleAwardEntity.builder()
                        .awardId(awardId)
                        .build();
            }

        }

        // 5. 默认抽奖流程
        // 进行默认抽奖
        Integer awardId = strategyDispatch.getRandomAwardId(strategyId);

        // 6. 抽奖中规则过滤
        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleCenterAction =
                doCheckRaffleCenterAction(RaffleFactorEntity.builder().userId(userId).strategyId(strategyId).build(),
                        strategy.getRuleModels());

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

    protected abstract RuleActionEntity<RuleActionEntity.RaffleBeforeEntity> doCheckRaffleBeforeLogic(
            RaffleFactorEntity raffleFactorEntity, String... logics);

    protected abstract RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterAction(
            RaffleFactorEntity raffleFactorEntity, String... logics
    );
}
