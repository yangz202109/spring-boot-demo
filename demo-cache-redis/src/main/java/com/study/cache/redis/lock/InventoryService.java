package com.study.cache.redis.lock;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 业务类
 * 调用购买方法减少redis中的库存数量
 * 1.0是在单机上,之后的是分布式微服务-->一台Nginx轮询方式代理2台服务,2台微服务高并发下访问各自的购买方法
 *
 * @author yangz
 * @date 2023/6/19 - 15:25
 */

@Slf4j
@Service
public class InventoryService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private DistributeLockFactory distributeLockFactory;

    private Lock lock;

    /**
     * 1.0版 在单机服务加锁(jvm),解决单机重复消费问题
     * 问题：当但是在分布式系统中因为竞争的线程可能不在同一个节点/服务器上(同一个jvm中)
     * lock锁是无效的
     */
    public String sale1() {
        lock = new ReentrantLock();
        StringBuilder retMessage = new StringBuilder();

        //加锁
        lock.lock();
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
        } finally {
            //解锁
            lock.unlock();
        }
        return retMessage.toString();
    }

    /**
     * 2.0版 在分布式系统中各个服务同时调用消费方法,引入redis命令解决重复消费问题
     * 问题：递归过多容易导致StackOverflow*
     */
    public String sale2() {

        StringBuilder retMessage = new StringBuilder();
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();

        //setnx命令如果当前key不存在,则添加成功返回true
        boolean flag = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue));

        if (!flag) {
            //没有抢到锁,则等待30毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
               log.error(e.getMessage());
            }
            //递归重试
            sale2();
        } else {
            //抢锁成功,执行正常业务逻辑
            try {
                //1.获取库存信息
                String result = (String) redisTemplate.opsForValue().get("inventory");
                //2.判断库存是否足够
                Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
                //3.扣减库存,每次减1
                if (inventoryNum > 0) {
                    redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                    retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
                } else {
                    retMessage.append("该商品已经售完");
                }
            } finally {
                //释放锁(删除key)
                redisTemplate.delete(key);
            }
        }
        return retMessage.toString();
    }

    /**
     * 2.1版 优化StackOverflow问题,使用自旋代替递归重试
     * 问题：当本服务抢锁成功后,没有执行到删除key可以就挂了,就会导致死锁
     */
    public String sale2_1() {

        StringBuilder retMessage = new StringBuilder();
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();

        //抢锁失败重复抢 开始自旋
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue))) {
            //没有抢到锁,则等待30毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        //抢锁成功,执行正常业务逻辑
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
        } finally {
            redisTemplate.delete(key);
        }

        return retMessage.toString();
    }

    /**
     * 2.2版 优化死锁问题
     * 添加key的过期时间
     * 问题：当A线程的业务处理超过key的过期时间,其所占的锁就自动被释放掉了,然后B线程抢到锁开始干活
     * 在B线程进行业务处理时,此时A线程完成业务处理就会删除B添加的锁
     */
    public String sale2_2() {

        StringBuilder retMessage = new StringBuilder();
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();

        //抢锁失败重复抢 自旋
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 10, TimeUnit.SECONDS))) {
            //没有抢到锁,则等待30毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        //当设置key及抢锁成功,立刻设置过期时间10秒--不应该分开key的添加和设置过期时间命令需要保证多条命令的原子性
        // redisTemplate.expire(key,10,TimeUnit.SECONDS);

        //抢锁成功,执行正常业务逻辑
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
        } finally {
            redisTemplate.delete(key);
        }
        return retMessage.toString();
    }

    /**
     * 3.0 修复误删,只能自己删除自己加的锁
     * 问题: 判断误删和删除的两条命令是分开执行不具有原子性
     */
    public String sale3() {

        StringBuilder retMessage = new StringBuilder();
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();

        //抢锁失败重复抢 自旋
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 10, TimeUnit.SECONDS))) {
            //没有抢到锁,则等待30毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        //抢锁成功,执行正常业务逻辑
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
        } finally {
            //改进,只能删除自己的key,不能删除别人的
            String value = (String) redisTemplate.opsForValue().get(key);
            if (StrUtil.isNotEmpty(value) && Objects.equals(value, uuidValue)) {
                redisTemplate.delete(key);
            }
        }
        return retMessage.toString();
    }

    /**
     * 4.0 通过lua脚本发送多条命令执行保证原子性
     * 问题: 不满足重入性
     */
    public String sale4() {
        StringBuilder retMessage = new StringBuilder();
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().threadId();

        //抢锁失败重复抢 自旋
        while (Boolean.FALSE.equals(redisTemplate.opsForValue().setIfAbsent(key, uuidValue, 10, TimeUnit.SECONDS))) {
            //没有抢到锁,则等待30毫秒
            try {
                TimeUnit.MILLISECONDS.sleep(30);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        //抢锁成功,执行正常业务逻辑
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
        } finally {
            //改进,修改为lua脚本的redis分布式锁调用,必须保证原子性
            String luaScript = "if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            /*
             DefaultRedisScript 参数1:lua脚本 参数2:脚本执行后的返回值类型
             key的集合
             参数数组
             */
            redisTemplate.execute(new DefaultRedisScript<>(luaScript, Integer.class), Collections.singletonList(key), uuidValue);
        }
        return retMessage.toString();
    }

    /**
     * 5.0 在1.0版上改成使用自定义的锁
     * 问题：获取锁是固定写死只能获取redis的分布式锁并且同一个线程的threadUUId不一致导致重入失败
     */
    public String sale5() {
        //获取自定义redis分布式锁类
        lock = new MyRedisLock(redisTemplate);
        StringBuilder retMessage = new StringBuilder();

        //加锁
        lock.lock();
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }

            //重入 有问题!!
            // testReEntry();
        } finally {
            //解锁
            lock.unlock();
        }
        return retMessage.toString();

    }

    /**
     * 6.0 通过工厂来获取对应实现的分布式锁
     * 可能后期会扩展使用其他技术来实现分布式锁 和修复重入失败
     * 问题：当前线程业务时间超过锁的自动过期时间,
     * 会出现当前线程还在执行业务时其他线程抢到锁重复消费库存,导致数据不一致或错误的结果
     */
    public String sale6() {
        //使用工厂模式来获取指定分布式锁
        lock = distributeLockFactory.getDistributeLock(DistributeLockTypeMenu.REDID);
        StringBuilder retMessage = new StringBuilder();

        //加锁
        lock.lock();
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }

            //重入
            testReEntry();
        } finally {
            //解锁
            lock.unlock();
        }
        return retMessage.toString();

    }

    /**
     * 7.0 自动续期(在任务执行时间较长的情况下,可以在获取锁后定期续约,即重新设置锁的过期时间,以防止锁过期而被其他线程获取)
     * 问题:多台redis(主从,哨兵或集群),在故障转移时可能会出现master存入的数据(锁)还未同步给slave时就宕机了,
     * 该数据(锁)就丢失了,这是redis的问题,因为redis是基于AP(可用性和分区容错性)架构
     */
    public String sale7() {
        //使用工厂模式来获取指定分布式锁
        lock = distributeLockFactory.getDistributeLock(DistributeLockTypeMenu.REDID);
        StringBuilder retMessage = new StringBuilder();

        //加锁
        lock.lock();
        try {
            //1.获取库存信息
            String result = (String) redisTemplate.opsForValue().get("inventory");
            //2.判断库存是否足够
            Integer inventoryNum = result == null ? 0 : Integer.parseInt(result);
            //3.扣减库存,每次减1
            if (inventoryNum > 0) {
                redisTemplate.opsForValue().set("inventory", String.valueOf(--inventoryNum));
                retMessage.append("成功卖出一商品,库存剩余: ").append(inventoryNum);
            } else {
                retMessage.append("该商品已经售完");
            }
            //重入
            testReEntry();
        } finally {
            //解锁
            lock.unlock();
        }
        return retMessage.toString();
    }

    /* TDD
     * 8.0 引入Redisson对应的官网推荐RedLock实现类
     */

    /**
     * 重入方法
     */
    private void testReEntry() {
        lock = distributeLockFactory.getDistributeLock(DistributeLockTypeMenu.REDID);
        //加锁
        lock.lock();
        try {
            System.out.println("=====进入testMethod方法,测试可重入=====");
        } finally {
            //解锁
            lock.unlock();
        }
    }
}