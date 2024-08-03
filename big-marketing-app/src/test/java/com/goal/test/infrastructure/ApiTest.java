package com.goal.test.infrastructure;

import com.goal.infrastructure.persistent.redis.IRedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@Slf4j
@SpringBootTest
public class ApiTest {

    @Autowired
    private IRedisService redisService;

    @Test
    public void test() {

        RMap<Object, Object> map = redisService.getMap("strategy_id_10001");

        map.put(1, 101);
        map.put(2, 202);
        map.put(3, 303);

        Object val = redisService.getFromMap("strategy_id_10001", 1);

        System.out.println(val);

    }

}
