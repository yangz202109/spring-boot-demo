package com.study.sharding.jdbc;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.study.sharding.jdbc.mapper.OrderMapper;
import com.study.sharding.jdbc.mapper.UserMapper;
import com.study.sharding.jdbc.model.Order;
import com.study.sharding.jdbc.model.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 测试sharding-jdbc分库分表
 * </p>
 * 逻辑表tb_order整体使用两个数据库,每个库中建3张结构相同相同的表,在操作tb_order数据时,会根据order_id字段值定位数据所属的分片节点；
 * </p>
 * 库路由db_${0..1}采用db_${order_id%2}的算法；
 * 表路由tb_order_${0..2}采用tb_order_${order_id%3}的算法；
 *
 * @author yangz
 * @date Created in 2019-03-26 13:44
 */
@Slf4j
@SpringBootTest
public class SpringBootDemoShardingJdbcApplicationTests {

    @Resource
    private UserMapper userMapper;
    @Resource
    private OrderMapper orderMapper;


    @Test
    public void saveUser() {
        //用户表主库操作
        User user = new User(1L, "张三", 22, LocalDateTime.now());
        userMapper.insert(user);
    }

    @Test
    public void saveOder() {
        //在对tb_order表执行增删改查时,会根据order_id的字段值计算库表的路由节点,注意分页时会查询所有的分库和分表,然后汇总查询的结果;
        Order order = new Order(1000L, 123456L, 20000L, "vip商品");
        orderMapper.insert(order);
    }

    @Test
    public void batchSaveOder() {
        List<Order> orders = new ArrayList<>(3);
        orders.add(new Order(1002L, 553456L, 78960L, "书籍"));
        orders.add(new Order(1003L, 553457L, 755454L, "书籍2"));
        orders.add(new Order(1004L, 553458L, 527852L, "书籍3"));
        orderMapper.insert(orders);
    }

    @Test
    public void queryOrderListDemo() {
        List<Order> orders = orderMapper.selectList(Wrappers.<Order>query().lambda().in(Order::getOrderId, 553456L, 553458L));
        log.info("orders : {}", orders);
    }
}

