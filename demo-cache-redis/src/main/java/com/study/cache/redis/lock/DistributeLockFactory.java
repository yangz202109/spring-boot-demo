package com.study.cache.redis.lock;

import cn.hutool.core.util.IdUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * @author yangz
 * @date 2023/6/29 - 14:24
 * 分布式锁工厂,基于工厂模式返回指定锁对象
 */
@Component
public class DistributeLockFactory {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String threadUUId;

    public DistributeLockFactory(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.threadUUId = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();
    }

    /**
     * 当前只支持redis的实现
     *
     * @param lockType 锁的种类
     * @return 指定锁对象
     */
    public Lock getDistributeLock(DistributeLockTypeMenu lockType) {
        switch (lockType) {
            case MYSQL:
                //TODO  MYSQL版的分布式锁
                return null;
            case REDID:
                return new MyRedisLock(redisTemplate, threadUUId);
            case ZOOKEEPER:
                //TODO  ZOOKEEPER版的分布式锁
                return null;
        }
        return null;
    }

}
