package com.goal.domain.strategy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 根据过滤结果判断是否接管后续
 */
@Getter
@AllArgsConstructor
public enum RuleLogicCheckTypeVO {

    ALLOW("0000", "放行；执行后续的流程，不受规则引擎影响"),
    TAKE_OVER("0001","接管；后续的流程，受规则引擎执行结果影响"),
    ;

    private final String code;
    private final String info;

    public static boolean isAllow(String code) {
        return code.equalsIgnoreCase(ALLOW.code);
    }

    public static boolean isTakeOver(String code) {
        return !isAllow(code);
    }
}
