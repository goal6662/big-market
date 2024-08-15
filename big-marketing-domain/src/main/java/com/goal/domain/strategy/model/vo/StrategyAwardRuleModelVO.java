package com.goal.domain.strategy.model.vo;

import com.goal.types.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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
        List<String> ruleModelList = new ArrayList<>();

        String[] ruleModelValues = ruleModels.split(Constants.SPLIT);

        for (String ruleModel : ruleModelValues) {
            if (ruleModel.equals(type)) {
                ruleModelList.add(ruleModel);
            }
        }

        return ruleModelList;
    }
}
