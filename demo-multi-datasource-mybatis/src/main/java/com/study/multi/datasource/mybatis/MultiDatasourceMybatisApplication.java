package com.study.multi.datasource.mybatis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-01-21 14:19
 */
@SpringBootApplication
@MapperScan(basePackages = "com.xkcoding.multi.datasource.mybatis.mapper")
public class MultiDatasourceMybatisApplication {

    public static void main(String[] args) {
        SpringApplication.run(MultiDatasourceMybatisApplication.class, args);
    }

}

