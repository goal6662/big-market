package com.goal.types.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Constants {

    public final static String SPLIT = ",";
    public final static String COLON = ":";
    public final static String SPACE = " ";
    public final static String UNDERLINE = "_";

    public static class RedisKey {
        public static String STRATEGY_KEY = "big_market_strategy_key_";
        public static String STRATEGY_AWARD_KEY = "big_market_strategy_award_key_";
        public static String STRATEGY_AWARD_LIST_KEY = "big_market_strategy_award_list_key_";
        public static String STRATEGY_RATE_TABLE_KEY = "big_market_strategy_rate_table_key_";
        public static String STRATEGY_RATE_RANGE_KEY = "big_market_strategy_rate_range_key_";
        public static String RULE_TREE_VO_KEY = "rule_tree_vo_key_";
        public static String STRATEGY_AWARD_COUNT_KEY = "strategy_award_count_key_";
        public static String STRATEGY_AWARD_COUNT_QUERY_KEY = "strategy_award_count_query_key";

    }

    @Getter
    @AllArgsConstructor
    public enum RuleModelEnum {
        RULE_LOCK("rule_lock"),
        RULE_LUCK_AWARD("rule_luck_award"),
        RULE_BLACKLIST("rule_blacklist"),
        RULE_WEIGHT("rule_weight"),
        RULE_RANDOM("rule_random"),
        ;
        private final String model;
    }
}
