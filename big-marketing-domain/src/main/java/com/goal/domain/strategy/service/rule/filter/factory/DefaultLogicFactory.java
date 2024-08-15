package com.goal.domain.strategy.service.rule.filter.factory;

import com.goal.domain.strategy.model.entity.RuleActionEntity;
import com.goal.domain.strategy.service.annotation.LogicStrategy;
import com.goal.domain.strategy.service.rule.filter.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 过滤策略工厂
 *  限定过滤规则
 *  加载过滤规则
 */
@Service
public class DefaultLogicFactory {

    private final Map<String, ILogicFilter<?>> logicFilterMap = new ConcurrentHashMap<>();

    public DefaultLogicFactory(List<ILogicFilter<?>> logicFilters) {

        logicFilters.forEach(logicFilter -> {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(logicFilter.getClass(), LogicStrategy.class);
            if (strategy != null) {
                logicFilterMap.put(strategy.logicModel().getCode(), logicFilter);
            }
        });

    }

    public <T extends RuleActionEntity.RaffleEntity> Map<String, ILogicFilter<T>> openLogicFilter() {
        return (Map<String, ILogicFilter<T>>) (Map<?, ?>) logicFilterMap;
    }


    @Getter
    @AllArgsConstructor
    public enum LogicModel {
        RULE_WEIGHT("rule_weight","【抽奖前规则】根据抽奖权重返回可抽奖范围KEY", "before"),
        RULE_BLACKLIST("rule_blacklist","【抽奖前规则】黑名单规则过滤，命中黑名单则直接返回", "before"),
        RULE_LOCK("rule_lock", "【抽奖中规则】抽中需要次数解锁的奖品后，执行接管", "center")
        ;

        private final String code;
        private final String info;
        private final String type;

        public static boolean isBefore(String ruleModel) {
            return ruleModel.equals(RULE_BLACKLIST.getCode())
                    || ruleModel.equals(RULE_WEIGHT.getCode());
        }

        public static boolean isCenter(String ruleModel) {
            return ruleModel.equals(RULE_LOCK.getCode());
        }

    }
}
