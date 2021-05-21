package com.carlos.ocean;

import com.carlos.ocean.mapper.UserMapper;
import com.carlos.ocean.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class OceanApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void map() {
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void redisTest() {

    }

}
