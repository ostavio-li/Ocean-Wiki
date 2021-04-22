package com.carlos.ocean;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class OceanApplicationTests {
    StringRedisTemplate template;
    @Test
    void contextLoads() {
    }

    @Test
    void redisTest() {

        template.opsForValue().set("li", "JJJ");
    }

}
