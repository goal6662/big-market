package com.goal.domain.strategy.repository;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.model.entity.StrategyRuleEntity;
import com.goal.domain.strategy.model.vo.RuleTreeVO;
import com.goal.domain.strategy.model.vo.StrategyAwardRuleModelVO;

import java.util.List;
import java.util.Map;

public interface IStrategyRepository {
    /**
     * 查询指定策略的奖品列表
     * @param strategyId 策略ID
     * @return 奖品列表
     */
    List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId);

    /**
     * 存储奖品概率表到 Redis
     */
    void storeStrategyAwardSearchTables(String key, int rateRange, Map<Integer, Integer> shuffledAwardSearchRateTables);

    /**
     * 获取指定策略概率表的大小
     *  普通策略【非权重策略】
     * @param strategyId 策略ID，用于生成 Redis 的 key
     * @return 大小
     */
    int getRateRange(Long strategyId);

    /**
     * 获取权重策略的概率表大小
     * @param key 权重策略 key
     */
    int getRateRange(String key);

    /**
     * 从策略表中随机获取一个奖品ID
     * @param strategyId 策略ID
     * @param rateIndex 奖品下标
     * @return 奖品ID
     */
    Integer getStrategyAwardAssemble(Long strategyId, int rateIndex);

    /**
     * 权重策略
     */
    Integer getStrategyAwardAssemble(String key, int rateIndex);

    /**
     * 查询策略类
     */
    StrategyEntity queryStrategyEntityByStrategyId(Long strategyId);

    /**
     * 查询某一策略的具体过滤规则
     * @param strategyId 策略ID
     * @param ruleModel 规则名称
     * @return 过滤规则
     */
    StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel);

    String queryStrategyRuleValue(Long strategyId, String ruleModel);

    StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId);

    /**
     * 查询决策树的根节点
     * @param treeId 规则模型
     * @return 决策树
     */
    RuleTreeVO queryRuleTreeVOByTreeId(String treeId);
}
