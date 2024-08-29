package com.study.rbac.security.util;

import cn.hutool.json.JSONUtil;
import com.study.rbac.security.SecurityApplicationTests;
import com.study.rbac.security.common.Consts;
import com.study.rbac.security.common.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 测试RedisUtil
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-11 20:44
 */
@Slf4j
public class RedisUtilTest extends SecurityApplicationTests {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void findKeysForPage() {
        PageResult pageResult = redisUtil.findKeysForPage(Consts.REDIS_JWT_KEY_PREFIX + Consts.SYMBOL_STAR, 2, 1);
        log.info("[pageResult]= {}", JSONUtil.toJsonStr(pageResult));
    }
}
