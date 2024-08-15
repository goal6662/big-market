package com.goal.domain.strategy.model.vo;

import com.goal.domain.strategy.service.rule.filter.factory.DefaultLogicFactory;
import com.goal.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分离出抽将前、中、后规则
 */
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StrategyAwardRuleModelVO {

    private String ruleModels;

    /**
     * 获取所有抽奖中过滤规则
     */
    public List<String> raffleCenterRuleModels() {
        return getRaffleModelsByType("center");
    }

    public List<String> raffleBeforeRuleModels() {
        return getRaffleModelsByType("before");
    }

    public List<String> raffleAfterRuleModels() {
        return getRaffleModelsByType("after");
    }

    private List<String> getRaffleModelsByType(String type) {
        Map<String, List<String>> raffleTypeMap = new HashMap<>();

        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);

        for (String ruleModel : ruleModelValues) {
            List<String> modelList = null;
            if (DefaultLogicFactory.LogicModel.isBefore(ruleModel)) {
                modelList = raffleTypeMap.getOrDefault("before", new ArrayList<>());
            } else if (DefaultLogicFactory.LogicModel.isCenter(ruleModel)) {
                modelList = raffleTypeMap.getOrDefault("center", new ArrayList<>());
            }

            if (modelList != null) {
                modelList.add(ruleModel);
                raffleTypeMap.put(ruleModel, modelList);
            }

        }

        return raffleTypeMap.get(type);
    }
}
