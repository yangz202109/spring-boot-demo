package com.xkcoding.mongodb;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-28 16:14
 */
@SpringBootApplication
public class SpringBootDemoMongodbApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDemoMongodbApplication.class, args);
    }

    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }

}

