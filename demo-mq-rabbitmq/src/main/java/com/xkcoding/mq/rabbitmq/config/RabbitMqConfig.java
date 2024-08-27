package com.xkcoding.mq.rabbitmq.config;

import com.google.common.collect.Maps;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;


/**
 * @author yangz
 * @date 2022/5/27 - 16:30
 * 创建简单交换机和队列
 */

@Configuration
public class RabbitMqConfig {

    //开启生产者事务
    @Bean
    public RabbitTransactionManager transactionManager(CachingConnectionFactory connectionFactory) {
        return new RabbitTransactionManager(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(CachingConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }


    /**
     * 创建一个直接类型的交换机
     */
    //@Bean
    public DirectExchange delayExchange() {
        return new DirectExchange("simple_direct_exchange");
    }

    /**
     * 创建一个扇出类型的交换机
     */
   // @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("simple_fanout_exchange");
    }

    /**
     * 创建一个主题类型的交换机
     */
    //@Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("simple_topic_exchange");
    }

    /**
     * 创建一个自定义类型的交换机
     */
   // @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = Maps.newHashMap();
        args.put("x-delayed-type", "direct");
        return new CustomExchange("fff", "x-delayed-message", true, false, args);
    }


    //@Bean
    public Queue Queue1() {
        return new Queue("queue1");
    }

}
