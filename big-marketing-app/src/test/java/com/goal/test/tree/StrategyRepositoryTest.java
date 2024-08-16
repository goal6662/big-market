package com.goal.test.tree;

import com.alibaba.fastjson2.JSON;
import com.goal.domain.strategy.model.vo.RuleTreeVO;
import com.goal.domain.strategy.repository.IStrategyRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class StrategyRepositoryTest {

    @Resource
    private IStrategyRepository strategyRepository;

    @Test
    public void queryRuleTreeVOByTreeId() {
        RuleTreeVO ruleTreeVO = strategyRepository.queryRuleTreeVOByTreeId("tree_lock");
        log.info("测试结果：{}", JSON.toJSONString(ruleTreeVO));
    }

}
