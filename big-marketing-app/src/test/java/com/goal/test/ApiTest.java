package com.goal.test;

import com.goal.domain.strategy.model.entity.RaffleFactorEntity;
import com.goal.domain.strategy.service.IRaffleStrategy;
import com.goal.infrastructure.persistent.dao.IAwardDao;
import com.goal.infrastructure.persistent.dao.IStrategyAwardDao;
import com.goal.infrastructure.persistent.dao.IStrategyDao;
import com.goal.infrastructure.persistent.dao.IStrategyRuleDao;
import com.goal.infrastructure.persistent.po.Award;
import com.goal.infrastructure.persistent.po.Strategy;
import com.goal.infrastructure.persistent.po.StrategyAward;
import com.goal.infrastructure.persistent.po.StrategyRule;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ApiTest {

    @Resource
    private IAwardDao strategyRuleDao;

    @Resource
    private IStrategyDao strategyDao;

    @Resource
    private IStrategyAwardDao strategyAwardDao;

    @Resource
    private IRaffleStrategy raffleStrategy;

    @Test
    public void test() {
//        List<Award> ruleList = strategyRuleDao.queryAwards();

//        List<Strategy> list = strategyDao.queryAll();

        List<StrategyAward> list = strategyAwardDao.queryAll();
        System.out.println(list);

        log.info("测试完成");
    }

    @Test
    public void raffleTest() {

        RaffleFactorEntity raffleFactor = RaffleFactorEntity.builder()
                .userId("user009")
                .strategyId(100001L)
                .build();

        System.out.println(raffleStrategy.performRaffle(raffleFactor));

    }

}
