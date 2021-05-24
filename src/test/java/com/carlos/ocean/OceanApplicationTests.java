package com.carlos.ocean;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class OceanApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void map() {
        System.out.println(passwordEncoder.encode("456"));
    }



    @Test
    void contextLoads() {
    }

    @Test
    void redisTest() {

    }

}
