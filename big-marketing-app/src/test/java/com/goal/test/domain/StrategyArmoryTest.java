package com.goal.test.domain;

import com.goal.domain.strategy.service.armory.IStrategyArmory;
import com.goal.domain.strategy.service.armory.IStrategyDispatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class StrategyArmoryTest {

    @Autowired
    private IStrategyArmory strategyArmory;

    @Autowired
    private IStrategyDispatch strategyDispatch;

    @BeforeEach
    public void testAssembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void testAwardIdGet() {
        for (int i = 0; i < 10; i++) {
            log.info("获取奖品：{}", strategyDispatch.getRandomAwardId(100002L));
        }

        for (int i = 0; i < 10; i++) {
            log.info("获取奖品：{}", strategyDispatch.getRandomAwardId(100002L, "6000"));
        }

    }

}
