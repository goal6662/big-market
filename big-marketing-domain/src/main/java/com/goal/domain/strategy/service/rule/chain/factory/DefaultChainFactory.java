package com.goal.domain.strategy.service.rule.chain.factory;

import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.rule.chain.ILogicChain;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于构建责任链
 *      建立起前后关系
 */
@Service
@RequiredArgsConstructor
public class DefaultChainFactory {

    // 原型模式获取对象
    private final ApplicationContext applicationContext;

    private final Map<Long, ILogicChain> logicChainGroup = new ConcurrentHashMap<>();

    protected final IStrategyRepository repository;

    /**
     * 通过策略ID，构建责任链
     *      责任链的顺序由数据库的声名的前后顺序决定
     * @param strategyId 策略ID
     * @return LogicChain
     */
    public ILogicChain openLogicChain(Long strategyId) {
        // 判断是否已经有缓存
        ILogicChain cacheLogicChain = logicChainGroup.get(strategyId);
        if (cacheLogicChain != null) {
            return cacheLogicChain;
        }

        StrategyEntity strategy = repository.queryStrategyEntityByStrategyId(strategyId);
        String[] ruleModels = strategy.getRuleModels();

        // 如果未配置策略规则，则只装填一个默认责任链
        if (null == ruleModels || 0 == ruleModels.length) {
            ILogicChain ruleDefaultLogicChain = applicationContext.getBean("default", ILogicChain.class);

            logicChainGroup.put(strategyId, ruleDefaultLogicChain);
            return ruleDefaultLogicChain;
        }

        // 按照配置顺序装填用户配置的责任链；rule_blacklist、rule_weight 「注意此数据从Redis缓存中获取，如果更新库表，记得在测试阶段手动处理缓存」
        ILogicChain logicChain = applicationContext.getBean(ruleModels[0], ILogicChain.class);
        ILogicChain current = logicChain;
        for (int i = 1; i < ruleModels.length; i++) {
            ILogicChain nextChain = applicationContext.getBean(ruleModels[i], ILogicChain.class);
            current = current.appendNext(nextChain);
        }

        // 责任链的最后装填默认责任链
        current.appendNext(applicationContext.getBean("default", ILogicChain.class));
        logicChainGroup.put(strategyId, logicChain);

        return logicChain;
    }

}
