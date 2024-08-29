package com.study.cache.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * <p>
 * 启动类
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-16 17:02
 */
@SpringBootApplication
@EnableCaching
public class CacheEhcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(CacheEhcacheApplication.class, args);
    }
}
