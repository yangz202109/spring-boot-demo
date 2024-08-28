package com.study.sharding.jdbc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-01-23 22:05
 */
@SpringBootApplication
@MapperScan("com.study.sharding.jdbc.mapper")
public class SpringBootDemoShardingJdbcApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoShardingJdbcApplication.class, args);
    }

}

