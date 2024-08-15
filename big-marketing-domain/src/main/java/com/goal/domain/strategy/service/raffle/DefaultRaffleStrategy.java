package com.goal.domain.strategy.service.raffle;

import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.model.entity.RuleMatterEntity;
import com.goal.domain.strategy.model.vo.RuleLogicCheckTypeVO;
import com.goal.domain.strategy.service.AbstractRaffleStrategy;
import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 实现规则过滤的应用：
 *  黑名单过滤比较特殊需要单独考虑
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    private DefaultLogicFactory logicFactory;

    @Override
    protected RuleActionEntity<RuleActionEntity.RaffleCenterEntity> doCheckRaffleCenterAction(RaffleFactorEntity raffleFactorEntity, List<String> logics) {

        Map<String, ILogicFilter<RuleActionEntity.RaffleCenterEntity>> logicFilterMap = logicFactory.openLogicFilter();

        RuleActionEntity<RuleActionEntity.RaffleCenterEntity> ruleAction = null;
        for (String logicModel : logics) {
            ILogicFilter<RuleActionEntity.RaffleCenterEntity> logicFilter = logicFilterMap.get(logicModel);

            RuleMatterEntity ruleMatterEntity = new RuleMatterEntity();
            ruleMatterEntity.setUserId(raffleFactorEntity.getUserId());
            ruleMatterEntity.setAwardId(raffleFactorEntity.getAwardId());
            ruleMatterEntity.setStrategyId(raffleFactorEntity.getStrategyId());
            ruleMatterEntity.setRuleModel(logicModel);

            ruleAction = logicFilter.filter(ruleMatterEntity);

            // 非放行规则顺序过滤
            if (!ruleAction.getCode().equals(RuleLogicCheckTypeVO.ALLOW.getCode())) {
                // 不放行，进行接管
                return ruleAction;
            }
        }

        return ruleAction;
    }
}
