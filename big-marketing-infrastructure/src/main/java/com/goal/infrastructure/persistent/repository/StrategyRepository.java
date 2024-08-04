package com.goal.infrastructure.persistent.repository;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.infrastructure.persistent.dao.IStrategyAwardDao;
import com.goal.infrastructure.persistent.po.StrategyAward;
import com.goal.infrastructure.persistent.redis.IRedisService;
import com.goal.types.common.Constants;
import lombok.AllArgsConstructor;
import org.redisson.api.RMap;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@AllArgsConstructor
public class StrategyRepository implements IStrategyRepository {

    private IStrategyAwardDao strategyAwardDao;

    private IRedisService redisService;

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
    public Integer getStrategyAwardAssemble(Long strategyId, int rateIndex) {
        return redisService.getFromMap(Constants.RedisKey.STRATEGY_RATE_TABLE_KEY + strategyId, rateIndex);
    }

}
