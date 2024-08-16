package com.goal.domain.strategy.service.raffle;

import com.goal.domain.strategy.model.vo.RuleTreeVO;
import com.goal.domain.strategy.model.vo.StrategyAwardRuleModelVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.AbstractRaffleStrategy;
import com.goal.domain.strategy.service.rule.chain.ILogicChain;
import com.goal.domain.strategy.service.rule.chain.factory.DefaultChainFactory;
import com.goal.domain.strategy.service.rule.tree.factory.DefaultTreeFactory;
import com.goal.domain.strategy.service.rule.tree.factory.engine.IDecisionTreeEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 实现规则过滤的应用：
 *  黑名单过滤比较特殊需要单独考虑
 */
@Slf4j
@Service
public class DefaultRaffleStrategy extends AbstractRaffleStrategy {

    @Resource
    private IStrategyRepository repository;

    @Resource
    private DefaultTreeFactory treeFactory;

    @Resource
    private DefaultChainFactory chainFactory;

    @Override
    protected DefaultChainFactory.StrategyAwardVO raffleLogicChain(String userId, Long strategyId) {
        ILogicChain logicChain = chainFactory.openLogicChain(strategyId);
        return logicChain.logic(userId, strategyId);
    }

    @Override
    protected DefaultTreeFactory.StrategyAwardVO raffleLogicTree(String userId, Long strategyId, Integer awardId) {
        StrategyAwardRuleModelVO strategyAwardRuleModelVO = repository.queryStrategyAwardRuleModelVO(strategyId, awardId);
        // 当前奖品无对应规则进行过滤
        if (strategyAwardRuleModelVO == null) {
            return DefaultTreeFactory.StrategyAwardVO
                    .builder()
                    .awardId(awardId)
                    .build();
        }

        RuleTreeVO ruleTreeVO = repository.queryRuleTreeVOByTreeId(strategyAwardRuleModelVO.getRuleModels());
        if (ruleTreeVO == null) {
            throw new RuntimeException("存在抽奖策略配置的规则模型 Key，未在库表 rule_tree、rule_tree_node、rule_tree_line 配置对应的规则树信息 " + strategyAwardRuleModelVO.getRuleModels());
        }

        IDecisionTreeEngine treeEngine = treeFactory.openLogicTree(ruleTreeVO);
        return treeEngine.process(userId, strategyId, awardId);
    }
}
