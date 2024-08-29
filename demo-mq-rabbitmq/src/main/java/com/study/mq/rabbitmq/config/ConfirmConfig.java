package com.study.mq.rabbitmq.config;

import com.study.mq.rabbitmq.constants.RabbitMqConst;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/28 - 10:02
 * 消息没有发送成功(生产者端)
 * 1.发布确认:   自定义消息发送成功或失败的回调处理,分别针对交换机和队列来确认,如果没有成功发送到消息队列服务器上,那就可以尝试重新发送
 * 2.备份交换机: 为目标交换机指定备份交换机,当目标交换机投递失败时,把消息投递至备份交换机
 * 3.持久化交换机和队列: 在创建交换机和队列时开启持久化(默认已经开启),确保不会随着RabbitMQ服务器重启而丢失
 *
 * 消息没有被消费(消费端): 把消息确认模式改为手动确认
 */
@Configuration
public class ConfirmConfig {


    /**
     * 创建交换机(关联一个备份交换机)
     */
    @Bean
    public DirectExchange confirmExchange() {
        /*设置确认交换机的备份交换机*/
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("alternate-exchange", RabbitMqConst.BACKUP_EXCHANGE);
        return new DirectExchange(RabbitMqConst.CONFIRM_EXCHANGE, true, false, arguments);
    }

    /**
     * 创建备份交换机
     * 备份交换机一定要选择fanout类型,因为原交换机转入备份交换机时并不会指定路由键
     */
    @Bean
    public FanoutExchange backupExchange() {
        return new FanoutExchange(RabbitMqConst.BACKUP_EXCHANGE);
    }

    /**
     * 创建队列
     */
    @Bean
    public Queue confirmQueue() {
        return new Queue(RabbitMqConst.CONFIRM_QUEUE);
    }

    /**
     * 创建备份队列
     */
    @Bean
    public Queue backupQueue() {
        return QueueBuilder.durable(RabbitMqConst.BACKUP_QUEUE).build();
    }

    /**
     * 创建报警队列
     */
    @Bean
    public Queue warningQueue() {
        return QueueBuilder.durable(RabbitMqConst.WARNING_QUEUE).build();
    }

    /**
     * 绑定
     */
    @Bean
    public Binding confirmQueueBindConfirmExchange() {
        return BindingBuilder.bind(confirmQueue()).to(confirmExchange()).with(RabbitMqConst.CONFIRM_ROUTING_KEY);
    }

    /**
     * 备份队列与备份交换机绑定
     */
    @Bean
    public Binding backupQueueBindBackupExchange() {
        return BindingBuilder.bind(backupQueue()).to(backupExchange());
    }

    /**
     * 报警队列与份备份交换机绑定
     */
    @Bean
    public Binding warningQueueBindBackupExchange() {
        return BindingBuilder.bind(warningQueue()).to(backupExchange());
    }
}
