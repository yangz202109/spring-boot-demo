package com.study.orm.mybatis.mapperPage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-08 13:43
 */
@SpringBootApplication
@MapperScan(basePackages = {"com.study.orm.mybatis.mapperPage.mapper"})
public class MybatisMapperPageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisMapperPageApplication.class, args);
    }
}
