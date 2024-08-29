package com.study.mq.rabbitmq;

import com.study.mq.rabbitmq.constants.RabbitMqConst;
import com.study.mq.rabbitmq.message.MessageStruct;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试Rabbitmq的七种工作模式
 */
@SpringBootTest
public class RabbitmqApplicationTests {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 测试发送到队列
     */
    @Test
    public void sendQueue() {
        //routingKey:简单模式直接传递队列名称
        rabbitTemplate.convertAndSend("SimpleQueue", new MessageStruct("hello world"));
    }

    /**
     * 测试简单发送
     */
    @Test
    public void sendSimple() {
        /*发送消息到普通交换机中绑定指定routingKey的队列*/
        rabbitTemplate.convertAndSend(RabbitMqConst.NORMAL_EXCHANGE, RabbitMqConst.NORMAL_ROUTING_KEY1, new MessageStruct("hello world"));
        rabbitTemplate.convertAndSend(RabbitMqConst.NORMAL_EXCHANGE, RabbitMqConst.NORMAL_ROUTING_KEY1, new MessageStruct("hello world"));
    }

    /**
     * 发送延迟消息(基于死信)
     */
    @Test
    public void sendMessageAndTime() {
        String time = "3000";

        /*发送消息到普通交换机中绑定指定routingKey的队列*/
        rabbitTemplate.convertAndSend(RabbitMqConst.NORMAL_EXCHANGE, RabbitMqConst.NORMAL_ROUTING_KEY3,
                new MessageStruct("hello world"), msg -> {
                    /*设置消息过期时间,以毫秒为单位*/
                    msg.getMessageProperties().setExpiration(time);
                    return msg;
                });
    }

    /**
     * 发送延迟消息
     */
    @Test
    public void sendDelayMsg() {
        String time = "3000";
        rabbitTemplate.convertAndSend(RabbitMqConst.DELAYED_EXCHANGE,
                RabbitMqConst.NORMAL_ROUTING_KEY3, new MessageStruct("hello world"),
                msg -> {
                    /*设置发送消息 延迟时长 单位 ms*/
                    // 设置延迟时间：以毫秒为单位
                    msg.getMessageProperties().setHeader("x-delay", time);
                    return msg;
                }
        );
    }

    /**
     * 事务消息(生产端)
     * 在生产者端使用事务消息和消费端没有关系,在生产者端使用事务消息仅仅是控制事务内的消息是否发送,提交事务就把事务内所有消息都发送到交换机
     * 回滚事务则事务内任何消息都不会被发送。
     * <p>
     * 因为在junit中给测试方法使用@Transactional注解默认就会回滚,所以回滚操作需要使用@RollBack注解操控
     */
    @Test
    @Transactional
    @Rollback(value = false)
    public void testSendMessageInTx() {

        // 1、发送第一条消息
        rabbitTemplate.convertAndSend(RabbitMqConst.NORMAL_EXCHANGE, RabbitMqConst.NORMAL_ROUTING_KEY1, new MessageStruct("hello world111"));

        // System.out.println(10/0);模拟异常 看第一条是否发送

        // 2、发送第二条消息
        rabbitTemplate.convertAndSend(RabbitMqConst.NORMAL_EXCHANGE, RabbitMqConst.NORMAL_ROUTING_KEY1, new MessageStruct("hello world222"));
    }
     
    @Test
    public void publish() {
        MessageStruct message = new MessageStruct("hello world");

        CorrelationData correlationData = new CorrelationData();
        correlationData.setId("1");
        rabbitTemplate.convertAndSend(RabbitMqConst.CONFIRM_EXCHANGE,
                RabbitMqConst.CONFIRM_ROUTING_KEY, message, correlationData);

        CorrelationData correlationData2 = new CorrelationData();
        correlationData2.setId("2");
        rabbitTemplate.convertAndSend(RabbitMqConst.CONFIRM_EXCHANGE,
                RabbitMqConst.CONFIRM_ROUTING_KEY + "1", message, correlationData2);
    }

}

