package com.goal.domain.strategy.model.entity;

import com.goal.types.common.Constants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class StrategyRuleEntity {

    /**
     * 抽奖策略ID
     */
    private Long strategyId;

    /**
     * 抽奖奖品ID【规则类型为策略，则不需要奖品ID】
     */
    private Integer awardId;

    /**
     * 抽象规则类型；1-策略规则、2-奖品规则
     */
    private Integer ruleType;

    /**
     * 抽奖规则类型【rule_random - 随机值计算、rule_lock - 抽奖几次后解锁、rule_luck_award - 幸运奖(兜底奖品)】
     */
    private String ruleModel;

    /**
     * 抽奖规则比值
     *  6000:102,103,104,105,106,107,108,109 5000:101,103
     */
    private String ruleValue;

    /**
     * 抽奖规则描述
     */
    private String ruleDesc;

    public Map<String, List<Integer>> getRuleWeightValues() {
        if (!"rule_weight".equals(ruleModel)) {
            return null;
        }

        String[] valueGroups = ruleValue.split(Constants.SPACE);
        Map<String, List<Integer>> ruleWeightValues = new HashMap<>();

        for (String valueGroup : valueGroups) {
            // 判断输入是否为空
            if (StringUtils.isBlank(valueGroup)) {
                return ruleWeightValues;
            }

            // 分割字符串获取键和值
            String[] pair = valueGroup.split(Constants.COLON);
            if (pair.length != 2) {
                throw new IllegalArgumentException("Invalid rule weight: " + ruleValue);
            }

            String[] values = pair[1].split(Constants.SPLIT);
            List<Integer> weightValues = new ArrayList<>();
            for (String value : values) {
                weightValues.add(Integer.valueOf(value));
            }

            ruleWeightValues.put(pair[0], weightValues);
        }

        return ruleWeightValues;
    }

}
