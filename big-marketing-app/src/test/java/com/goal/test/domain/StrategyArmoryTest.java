package com.goal.test.domain;

import com.goal.domain.strategy.service.armory.IStrategyArmory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class StrategyArmoryTest {

    @Resource
    private IStrategyArmory strategyArmory;


    @Test
    public void testAssembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100002L);
    }

    @Test
    public void testAwardIdGet() {
        for (int i = 0; i < 100; i++) {
            log.info("获取奖品：{}", strategyArmory.getRandomAwardId(100002L));
        }

    }

}
