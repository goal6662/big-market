package com.goal.domain.strategy.service.armory.impl;

import com.goal.domain.strategy.model.entity.StrategyAwardEntity;
import com.goal.domain.strategy.model.entity.StrategyEntity;
import com.goal.domain.strategy.model.entity.StrategyRuleEntity;
import com.goal.domain.strategy.repository.IStrategyRepository;
import com.goal.domain.strategy.service.armory.IStrategyArmory;
import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import com.goal.types.enums.ResponseCode;
import com.goal.types.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;

@Slf4j
@Service
public class StrategyArmoryDispatchImpl implements IStrategyArmory, IStrategyDispatch {

    @Resource
    private IStrategyRepository repository;

    @Override
    public boolean assembleLotteryStrategy(Long strategyId) {
        // 查询策略配置
        List<StrategyAwardEntity> strategyAwardEntityList = repository.queryStrategyAwardList(strategyId);
        assembleLotteryStrategy(String.valueOf(strategyId), strategyAwardEntityList);

        // 权重策略配置 主要是【rule_weight】
        StrategyEntity strategyEntity = repository.queryStrategyEntityByStrategyId(strategyId);
        String ruleModel = strategyEntity.getRuleWeight();
        if (ruleModel == null) {
            return true;
        }

        StrategyRuleEntity strategyRuleEntity = repository.queryStrategyRule(strategyId, ruleModel);
        if (strategyRuleEntity == null) {
            throw new AppException(ResponseCode.RULE_WEIGHT_NOT_CONFIG);
        }

        Map<String, List<Integer>> ruleWeightValues = strategyRuleEntity.getRuleWeightValues();
        // 为每一个权重都生成一个概率表
        for (String key : ruleWeightValues.keySet()) {

            List<Integer> ruleWeightValueList = ruleWeightValues.get(key);

            List<StrategyAwardEntity> strategyAwardEntityListClone = new ArrayList<>(strategyAwardEntityList);
            strategyAwardEntityListClone.removeIf(strategyAwardEntity ->
                    !ruleWeightValueList.contains(strategyAwardEntity.getAwardId()));

            assembleLotteryStrategy(strategyId + "_" + key, strategyAwardEntityListClone);

        }


        return true;
    }


    @Override
    public Integer getRandomAwardId(Long strategyId) {
        // 获取随机数范围
        int rateRange = repository.getRateRange(strategyId);
        return repository.getStrategyAwardAssemble(strategyId, new SecureRandom().nextInt(rateRange));
    }

    @Override
    public Integer getRandomAwardId(Long strategyId, String ruleWeightValue) {
        String key = strategyId + "_" + ruleWeightValue;

        int rateRange = repository.getRateRange(key);
        return repository.getStrategyAwardAssemble(key, new SecureRandom().nextInt(rateRange));
    }


    private void assembleLotteryStrategy(String strategyId, List<StrategyAwardEntity> strategyAwardEntityList) {
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
        List<Integer> strategySearchRateTables = getStrategySearchRateTables(totalAwardRate, minAwardRate, strategyAwardEntityList);

        // 6. 打乱顺序
        Collections.shuffle(strategySearchRateTables);

        Map<Integer, Integer> shuffledAwardSearchRateTables = new HashMap<>();
        for (int i = 0; i < strategySearchRateTables.size(); i++) {
            shuffledAwardSearchRateTables.put(i, strategySearchRateTables.get(i));
        }

        // 7. 存储概率查找表到Redis
        repository.storeStrategyAwardSearchTables(strategyId, shuffledAwardSearchRateTables.size(), shuffledAwardSearchRateTables);
    }

    /**
     * 获取概率表
     *
     * @param totalAwardRate          总的概率和 不一定为 1
     * @param minAwardRate            最小的概率
     * @param strategyAwardEntityList 当前策略下的奖品集合
     * @return 奖品对应的概率表
     */
    private List<Integer> getStrategySearchRateTables(BigDecimal totalAwardRate, BigDecimal minAwardRate,
                                                      List<StrategyAwardEntity> strategyAwardEntityList) {

        // 5. 获取槽的总个数
        BigDecimal rateRange = totalAwardRate.divide(minAwardRate, 0, RoundingMode.CEILING);

        // 总的位置集合
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
        return strategySearchRateTables;
    }

}
