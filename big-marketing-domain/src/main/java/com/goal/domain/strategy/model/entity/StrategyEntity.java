package com.goal.domain.strategy.model.entity;

import com.goal.types.common.Constants;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * 策略实体
 *  对应 strategy 表
 */
@Data
public class StrategyEntity {

    /**
     * 抽奖策略ID
     */
    private String strategyId;

    /**
     * 抽奖策略描述
     */
    private String strategyDesc;

    /**
     * 规则模型，rule配置的模型同步到此表，便于使用
     */
    private String ruleModels;

    /**
     * 获取所有的权重类型
      * @return 权重列表集合
     */
    public String[] getRuleModels() {
        if (StringUtils.isBlank(ruleModels)) {
            return null;
        }

        return ruleModels.split(Constants.SPLIT);
    }

    public String getRuleWeight() {
        String[] models = this.getRuleModels();
        if (models == null) {
            return null;
        }

        for (String model : models) {
            if ("rule_weight".equals(model)) {
                return model;
            }
        }
        return null;
    }

}
