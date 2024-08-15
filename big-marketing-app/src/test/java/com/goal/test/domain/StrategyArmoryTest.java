package com.goal.test.domain;

import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.service.IRaffleStrategy;
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

    @Autowired
    private IRaffleStrategy raffleStrategy;

    @BeforeEach
    public void testAssembleLotteryStrategy() {
        strategyArmory.assembleLotteryStrategy(100001L);
    }

    @Test
    public void testAwardIdGet() {

        RaffleFactorEntity raffleFactor = RaffleFactorEntity.builder()
                .strategyId(100001L)
                .userId("user110")
                .build();

        System.out.println(raffleStrategy.performRaffle(raffleFactor));

    }

}
