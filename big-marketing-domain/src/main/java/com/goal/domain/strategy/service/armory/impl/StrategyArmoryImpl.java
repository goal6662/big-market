package com.goal.domain.strategy.service.armory.impl;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmoryImpl implements IStrategyArmory {

    @Resource
    private IStrategyRepository repository;

    @Override
    public void assembleLotteryStrategy(Long strategyId) {
        // 1. 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntityList = repository.queryStrategyAwardList(strategyId);

        // 2. 获取最小概率值
        BigDecimal minAwardRate = strategyAwardEntityList.stream().map(StrategyAwardEntity::getAwardRate)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        // 3. 获取概率值总和
        BigDecimal totalAwardRate = strategyAwardEntityList.stream()
                .map(StrategyAwardEntity::getAwardRate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. 保证最小概率的奖品占一个位置，获取总的位置数目
        // 上取整
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 5. 总的位置集合
        List<Integer> strategySearchRateTables = new ArrayList<>(rateRange.intValue());
        for (StrategyAwardEntity strategyAward : strategyAwardEntityList) {

            Integer awardId = strategyAward.getAwardId();
            BigDecimal awardRate = strategyAward.getAwardRate();

            // 计算每个奖品占用位置的数量
            int awardRateRange = rateRange.multiply(awardRate).setScale(0, RoundingMode.CEILING).intValue();
            for (int i = 0; i < awardRateRange; i++) {
                strategySearchRateTables.add(awardId);
            }

        }

        // 6. 打乱顺序
        Collections.shuffle(strategySearchRateTables);

        Map<Integer, Integer> shuffledAwardSearchRateTables = new HashMap<>();
        for (int i = 0; i < strategySearchRateTables.size(); i++) {
            shuffledAwardSearchRateTables.put(i, strategySearchRateTables.get(i));
        }

        // 7. 存储概率查找表到Redis
        repository.storeStrategyAwardSearchTables(strategyId, shuffledAwardSearchRateTables.size(), shuffledAwardSearchRateTables);
    }

    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 获取随机数范围
        int rateRange = repository.getRateRange(strategyId);
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

}