package com.study.multi.datasource.jpa.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 雪花算法生成器
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-01-18 15:50
 */
@Configuration
public class SnowflakeConfig {
    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }
}
