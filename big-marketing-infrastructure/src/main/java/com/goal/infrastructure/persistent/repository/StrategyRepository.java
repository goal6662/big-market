package com.goal.infrastructure.persistent.repository;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.model.entity.StrategyRuleEntity;
import com.goal.domain.strategy.model.vo.*;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.infrastructure.persistent.dao.*;
import com.goal.infrastructure.persistent.po.*;
import com.goal.infrastructure.persistent.redis.IRedisService;
import com.goal.types.common.Constants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class StrategyRepository implements IStrategyRepository {

    private final IStrategyDao strategyDao;

    private final IStrategyAwardDao strategyAwardDao;

    private final IStrategyRuleDao strategyRuleDao;

    private final IRuleTreeDao ruleTreeDao;

    private final IRuleTreeNodeDao ruleTreeNodeDao;

    private final IRuleTreeNodeLineDao ruleTreeNodeLineDao;

    private final IRedisService redisService;

    @Override
    public List<StrategyAwardEntity> queryStrategyAwardList(Long strategyId) {

        // 优先从缓存中查取
        String cacheKey = Constants.RedisKey.STRATEGY_AWARD_KEY + strategyId;
        List<StrategyAwardEntity> strategyAwardEntityList = redisService.getValue(cacheKey);
        if (strategyAwardEntityList != null) {
            return strategyAwardEntityList;
        }

        // 缓存中不存在，从数据库查询
        List<StrategyAward> strategyAwardList =  strategyAwardDao.queryByStrategyId(strategyId);

        strategyAwardEntityList = new ArrayList<>();
        for (StrategyAward item : strategyAwardList) {
            StrategyAwardEntity entity = new StrategyAwardEntity();
            BeanUtils.copyProperties(item, entity);

            strategyAwardEntityList.add(entity);
        }

        return strategyAwardEntityList;
    }

    @Override
    public void storeStrategyAwardSearchTables(String key, int rateRange, Map<Integer, Integer> shuffledAwardSearchRateTables) {
        String rateCacheKey = Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key;

        // 存储范围值，用于后续生成随机数
        redisService.setValue(rateCacheKey, rateRange);

        String rateTableCacheKey = Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key;
        RMap<Object, Object> cacheRateTable = redisService.getMap(rateTableCacheKey);
        cacheRateTable.putAll(shuffledAwardSearchRateTables);
    }

    @Override
    public int getRateRange(Long strategyId) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + strategyId);
    }

    @Override
    public int getRateRange(String key) {
        return redisService.getValue(Constants.RedisKey.STRATEGY_RATE_RANGE_KEY + key);
    }

    @Override
    public Integer getStrategyAwardAssemble(Long strategyId, int rateIndex) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateIndex);
    }

    @Override
    public Integer getStrategyAwardAssemble(String key, int rateIndex) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + key, rateIndex);
    }

    @Override
    public StrategyEntity queryStrategyEntityByStrategyId(Long strategyId) {

        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyEntity strategyEntity = redisService.getValue(cacheKey);
        if (strategyEntity != null) {
            return strategyEntity;
        }

        Strategy strategy = strategyDao.queryByStrategyId(strategyId);

        strategyEntity = new StrategyEntity();
        BeanUtils.copyProperties(strategy, strategyEntity);

        return strategyEntity;
    }

    @Override
    public StrategyRuleEntity queryStrategyRule(Long strategyId, String ruleModel) {

        // 优先从缓存中获取
        String cacheKey = Constants.RedisKey.STRATEGY_KEY + strategyId;
        StrategyRuleEntity strategyRuleEntity = redisService.getValue(cacheKey);
        if (strategyRuleEntity != null) {
            return strategyRuleEntity;
        }

        // 查询参数
        StrategyRule strategyRuleReq = new StrategyRule();
        strategyRuleReq.setRuleModel(ruleModel);
        strategyRuleReq.setStrategyId(strategyId);

        strategyRuleReq = strategyRuleDao.queryStrategyRule(strategyRuleReq);

        // 拷贝属性
        strategyRuleEntity = new StrategyRuleEntity();
        BeanUtils.copyProperties(strategyRuleReq, strategyRuleEntity);

        return strategyRuleEntity;
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, Integer awardId, String ruleModel) {
        return strategyRuleDao.queryStrategyRuleValue(strategyId, awardId, ruleModel);
    }

    @Override
    public String queryStrategyRuleValue(Long strategyId, String ruleModel) {
        return strategyRuleDao.queryStrategyRuleValue(strategyId, null, ruleModel);
    }

    @Override
    public StrategyAwardRuleModelVO queryStrategyAwardRuleModelVO(Long strategyId, Integer awardId) {
        StrategyAward strategyAward = new StrategyAward();
        strategyAward.setStrategyId(strategyId);
        strategyAward.setAwardId(awardId);

        String ruleModels = strategyAwardDao.queryStrategyAwardRuleModels(strategyAward);
        return StrategyAwardRuleModelVO.builder().ruleModels(ruleModels).build();
    }

    @Override
    public RuleTreeVO queryRuleTreeVOByTreeId(String treeId) {
        // 优先从缓存获取
        String cacheKey = Constants.RedisKey.RULE_TREE_VO_KEY + treeId;
        RuleTreeVO ruleTreeVOCache = redisService.getValue(cacheKey);
        if (null != ruleTreeVOCache) return ruleTreeVOCache;

        // 从数据库获取
        RuleTree ruleTree = ruleTreeDao.queryRuleTreeByTreeId(treeId);
        List<RuleTreeNode> ruleTreeNodes = ruleTreeNodeDao.queryRuleTreeNodeListByTreeId(treeId);
        List<RuleTreeNodeLine> ruleTreeNodeLines = ruleTreeNodeLineDao.queryRuleTreeNodeLineListByTreeId(treeId);

        // 1. tree node line 转换Map结构
        Map<String, List<RuleTreeNodeLineVO>> ruleTreeNodeLineMap = new HashMap<>();
        for (RuleTreeNodeLine ruleTreeNodeLine : ruleTreeNodeLines) {
            RuleTreeNodeLineVO ruleTreeNodeLineVO = RuleTreeNodeLineVO.builder()
                    .treeId(ruleTreeNodeLine.getTreeId())
                    .ruleNodeFrom(ruleTreeNodeLine.getRuleNodeFrom())
                    .ruleNodeTo(ruleTreeNodeLine.getRuleNodeTo())
                    .ruleLimitType(RuleLimitTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitType()))
                    .ruleLimitValue(RuleLogicCheckTypeVO.valueOf(ruleTreeNodeLine.getRuleLimitValue()))
                    .build();

            List<RuleTreeNodeLineVO> ruleTreeNodeLineVOList = ruleTreeNodeLineMap.computeIfAbsent(ruleTreeNodeLine.getRuleNodeFrom(), k -> new ArrayList<>());
            ruleTreeNodeLineVOList.add(ruleTreeNodeLineVO);

        }

        // 2. tree node 转换为Map结构
        Map<String, RuleTreeNodeVO> treeNodeMap = new HashMap<>();
        for (RuleTreeNode ruleTreeNode : ruleTreeNodes) {
            RuleTreeNodeVO ruleTreeNodeVO = RuleTreeNodeVO.builder()
                    .treeId(ruleTreeNode.getTreeId())
                    .ruleKey(ruleTreeNode.getRuleKey())
                    .ruleDesc(ruleTreeNode.getRuleDesc())
                    .ruleValue(ruleTreeNode.getRuleValue())
                    .treeNodeLineVOList(ruleTreeNodeLineMap.get(ruleTreeNode.getRuleKey()))
                    .build();
            treeNodeMap.put(ruleTreeNode.getRuleKey(), ruleTreeNodeVO);
        }

        // 3. 构建 Rule Tree
        RuleTreeVO ruleTreeVODB = RuleTreeVO.builder()
                .treeId(ruleTree.getTreeId())
                .treeName(ruleTree.getTreeName())
                .treeDesc(ruleTree.getTreeDesc())
                .treeRootRuleNode(ruleTree.getTreeNodeRuleKey())
                .treeNodeMap(treeNodeMap)
                .build();

        redisService.setValue(cacheKey, ruleTreeVODB);
        return ruleTreeVODB;

    }

}
