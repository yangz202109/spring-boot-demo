package com.xkcoding.neo4j.config;

import cn.hutool.core.util.IdUtil;
import org.springframework.data.neo4j.core.schema.IdGenerator;

/**
 * <p>
 * 自定义主键策略
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-24 14:40
 */
public class CustomIdStrategy implements IdGenerator<String> {

    @Override
    public String generateId(String primaryLabel, Object entity) {
         return IdUtil.fastUUID();
    }
}
