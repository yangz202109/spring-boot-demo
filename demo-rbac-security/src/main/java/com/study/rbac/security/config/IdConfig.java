package com.study.rbac.security.config;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 雪花主键生成器
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-10 11:28
 */
@Configuration
public class IdConfig {
    /**
     * 雪花生成器
     */
    @Bean
    public Snowflake snowflake() {
        return IdUtil.getSnowflake(1, 1);
    }
}
