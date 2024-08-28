package com.xkcoding.cache.redis.lock;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author yangz
 * @date 2023/6/28 - 14:43
 * 自研的分布式锁,实现lock接口
 * 使用redis的hash储存  HSET key(锁名称) field(占用该锁的线程标识) value(加锁次数)
 */
public class MyRedisLock implements Lock {

    private static final Logger log = LoggerFactory.getLogger(MyRedisLock.class);
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * redis中hash的key
     */
    private final String lockName;

    /**
     * redis中hash的field
     */
    private final String threadUUId;

    /**
     * 过期时间
     */
    private final long expireTime;

    /**
     * 问题: 多次获取锁时,同一线程的threadUUId会不一致,回导致第二次加锁失败
     */
    public MyRedisLock(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.lockName = "redisLock";
        //5.0版重入失败原因所在
        this.threadUUId = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();
        this.expireTime = 30L;
    }

    /**
     * 让threadUUId通过工厂类传过来,保证数据在多次加锁时相同 在同一线程不管重入几次threadUUId相同
     */
    public MyRedisLock(RedisTemplate<String, Object> redisTemplate, String threadUUId) {
        this.redisTemplate = redisTemplate;
        this.lockName = "redisLock";
        this.threadUUId = threadUUId;
        this.expireTime = 30L;
    }

    @Override
    public void lock() {
        tryLock();
    }

    /**
     * 1.当任务执行完成后，计数器减1.
     * 2.当计数器减为0时，表示该线程已完全释放锁，可以删除锁的键.
     */
    @Override
    public void unlock() {
        //解锁脚本(key中的field不存在返回null,存在则执行递减1操作后当value等于0就删除key返回1否则返回0)
        String script = "if redis.call('hexists',KEYS[1],ARGV[1]) == 0 then return nil elseif redis.call('hincrby',KEYS[1],ARGV[1],-1) == 0 then redis.call('del',KEYS[1]) return 1 else return 0 end";
        Integer execute = redisTemplate.execute(new DefaultRedisScript<>(script, Integer.class), Collections.singletonList(lockName), threadUUId);
        if (Objects.isNull(execute)) {
            throw new RuntimeException("this lock doesn't exists!");
        }
    }

    @Override
    public boolean tryLock() {
        try {
            return tryLock(-1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.info("获取锁失败");
            return false;
        }
    }

    /**
     * 1.当线程第一次获取锁时，将锁的值设置为当前线程的唯一标识（uuID:线程ID）.
     * 2.在锁的值中添加一个计数器,初始值为1.
     * 3.检查锁的值是否为当前线程的唯一标识. 如果是,则将计数器加1,并继续执行任务.
     * 通过添加计数器,可以在同一线程内实现对锁的可重入,而不会被阻塞.
     * 这样即使同一线程多次获取锁,也能够正确地释放锁,避免死锁和其他问题.
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time == -1 && unit.equals(TimeUnit.SECONDS)) {
            //加锁脚本(key不存在或key中的field存在执行命令返回1)
            String script = "if redis.call('exists',KEYS[1]) == 0 or redis.call('hexists',KEYS[1],ARGV[1]) == 1 then " +
                    "redis.call('hincrby',KEYS[1],ARGV[1],1) " +
                    "redis.call('expire',KEYS[1],ARGV[2]) " +
                    "return 1 " +
                    "else " +
                    "return 0 " +
                    "end";

            Integer execute = redisTemplate.execute(new DefaultRedisScript<>(script, Integer.class), Collections.singletonList(lockName), threadUUId, StrUtil.toString(expireTime));
            if (Objects.nonNull(execute)) {
                while (execute == 0) {
                    //没有抢到锁,则等待40毫秒后自旋
                    try {
                        TimeUnit.MILLISECONDS.sleep(40);
                    } catch (InterruptedException e) {
                        log.info(e.getMessage());
                    }
                    execute = redisTemplate.execute(new DefaultRedisScript<>(script, Integer.class), Collections.singletonList(lockName), threadUUId, StrUtil.toString(expireTime));
                }

                //加锁成功调用定时刷新过期时间方法
                renewExpire();
            }
            return true;
        }
        return false;
    }

    /**
     * 重新刷新过期时间
     */
    private void renewExpire() {
        //更新过期时间脚本
        String script = "if redis.call('hexists',KEYS[1],ARGV[1]) == 1 then " +
                "return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end";

        /**
         * 安排指定的任务在指定的延迟后执行.
         * 形参：
         *  task —  要安排的任务.
         *  delay – 执行任务之前的延迟（以毫秒为单位）
         * <p>
         * 10秒后执行续期
         */
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Boolean execute = redisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockName), threadUUId, StrUtil.toString(expireTime));
                //执行脚本,返回成功再次调用renewExpire方法,反之退出(当key被删除时)
                if (Boolean.TRUE.equals(execute)) {
                    renewExpire();
                }
            }
        }, (this.expireTime * 1000) / 3);
    }

    @Override
    public void lockInterruptibly() {
        //用不到
    }

    @Override
    public Condition newCondition() {
        //用不到
        return null;
    }
}
