package com.carlos.ocean;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Carlos.Li
 */

@SpringBootApplication
@MapperScan("com.carlos.ocean.mapper")
public class OceanApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanApplication.class, args);
    }

}
