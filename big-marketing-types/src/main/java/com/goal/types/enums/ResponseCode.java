package com.goal.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ResponseCode {

    SUCCESS("0000", "成功"),
    UN_ERROR("0001", "未知失败"),
    ILLEGAL_PARAMETER("0002", "非法参数"),
    STRATEGY_NOT_ARMORY("0003", "抽奖策略未装配"),

    RULE_WEIGHT_NOT_CONFIG("biz-error-001", "抽奖权重存在，但未配置")
    ;

    private String code;
    private String info;

}
