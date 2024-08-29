package com.study.mq.rabbitmq.config;

import com.study.mq.rabbitmq.constants.RabbitMqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/24 - 14:24
 * 死信: 当一个消息无法被消费,它就变成了死信
 *
 * 给消息设定一个过期时间,超过这个时间没有被取走的消息就会被删除(没有关联死信交换机时)
 *
 */
@Configuration
public class DeadLetterConfig {

    /**创建普通交换机*/
    @Bean
    public DirectExchange normalExchange() {
        return new DirectExchange(RabbitMqConst.NORMAL_EXCHANGE);
    }

    /**创建死信交换机*/
    @Bean
    public DirectExchange deadExchange() {
        return new DirectExchange(RabbitMqConst.DEAD_EXCHANGE);
    }


    /**创建普通队列1-过期时间10秒
     * 给队列设置过期时间,表示这个队列中的消息全部使用同一个过期时间
     * */
    @Bean
    public Queue normalQueue1() {
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        /* 关联一个死信交换机,当该队列中的消息过期后就转入死信交换机*/
        arguments.put("x-dead-letter-exchange", RabbitMqConst.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", RabbitMqConst.DEAD_ROUTING_KEY);
        //该队列存放消息的时间ms
        arguments.put("x-message-ttl", 10000);
        return new Queue(RabbitMqConst.NORMAL_QUEUE1, false, false, false, arguments);
    }

    /**创建普通队列2-过期时间40秒*/
    @Bean
    public Queue normalQueue2() {
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", RabbitMqConst.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key",RabbitMqConst.DEAD_ROUTING_KEY);
        //该队列存放消息的时间ms
        arguments.put("x-message-ttl", 40000);
        return new Queue(RabbitMqConst.NORMAL_QUEUE2, false, false, false, arguments);
    }

    /**创建普通队列3*/
    @Bean
    public Queue normalQueue3(){
        /*设置关联的死信交换机*/
        Map<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange", RabbitMqConst.DEAD_EXCHANGE);
        arguments.put("x-dead-letter-routing-key", RabbitMqConst.DEAD_ROUTING_KEY);
        //不应该在队列中设置过期时间
        return new Queue(RabbitMqConst.NORMAL_QUEUE3, false, false, false, arguments);
    }

    /**队列1绑定交换机*/
    @Bean
    public Binding bindNormalQueue1AndExchange() {
        return BindingBuilder.bind(normalQueue1()).to(normalExchange()).with(RabbitMqConst.NORMAL_ROUTING_KEY1);
    }

    /**队列2绑定交换机*/
    @Bean
    public Binding bindNormalQueue2AndExchange() {
        return BindingBuilder.bind(normalQueue2()).to(normalExchange()).with(RabbitMqConst.NORMAL_ROUTING_KEY2);
    }

    /**队列3绑定交换机*/
    @Bean
    public Binding bindNormalQueue3AndExchange(){
        return BindingBuilder.bind(normalQueue3()).to(normalExchange()).with(RabbitMqConst.NORMAL_ROUTING_KEY3);
    }

    /**创建死信队列*/
    @Bean
    public Queue deadQueue() {
        return new Queue(RabbitMqConst.DEAD_QUEUE);
    }

    /**死信队列绑定死信交换机*/
    @Bean
    public Binding deadQueueBind() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange()).with(RabbitMqConst.DEAD_ROUTING_KEY);
    }
}