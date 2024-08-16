package com.goal.domain.strategy.service.rule.chain.impl;

import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.rule.chain.AbstractLogicChain;
import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.goal.types.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component("rule_blacklist")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class BackListLogicChain extends AbstractLogicChain {

    @Resource
    private IStrategyRepository repository;


    /**
     * 查询黑名单规则配置
     *      获取 rule_value 的值，判断用户是否在其中
     */
    @Override
    public DefaultChainFactory.StrategyAwardVO logic(String userId, Long strategyId) {
        log.info("抽奖责任链-黑名单开始 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        // 查询 rule_value
        String ruleValue = repository.queryStrategyRuleValue(strategyId, ruleModel());
        String[] splitRuleValue = ruleValue.split(Constants.COLON);

        // 获取奖品 ID
        int awardId = Integer.parseInt(splitRuleValue[0]);
        String[] userBlackIdList = splitRuleValue[1].split(Constants.SPLIT);
        for (String userBlackId : userBlackIdList) {
            if (userId.equals(userBlackId)) {
                log.info("抽奖责任链-黑名单接管 userId: {} strategyId: {} ruleModel: {} awardId: {}", userId, strategyId, ruleModel(), awardId);
                return DefaultChainFactory.StrategyAwardVO.builder()
                        .awardId(awardId)
                        .logicModel(ruleModel())
                        .build();
            }
        }

        // 过滤其他责任链
        log.info("抽奖责任链-黑名单放行 userId: {} strategyId: {} ruleModel: {}", userId, strategyId, ruleModel());

        return next().logic(userId, strategyId);
    }

    @Override
    protected String ruleModel() {
        return "rule_blacklist";
    }

}
