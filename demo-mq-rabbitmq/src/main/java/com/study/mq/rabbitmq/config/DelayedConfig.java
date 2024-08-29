package com.study.mq.rabbitmq.config;

import com.study.mq.rabbitmq.constants.RabbitMqConst;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2022/5/27 - 16:30
 * 创建延迟交换机和队列,并绑定
 * 注意：使用rabbitmq_delayed_message_exchange插件后,即使消息成功发送到队列上,也会导致returnedMessage()方法执行
 */
@Configuration
public class DelayedConfig {


    /**创建延迟交换机, x-delayed-type 和 x-delayed-message 固定 */
    @Bean
    public CustomExchange delayedExchange() {
        //x-delayed-type来指定交换机本身的类型(直接)
        Map<String, Object> arguments = new HashMap<>(1);
        arguments.put("x-delayed-type", "direct");

        /*
         * 创建自定义交换机
         * String name ： 交换机名称
         * String type  ：交换机类型
         * boolean durable ：是否持久化
         * boolean autoDelete ：是否自动删除
         * arguments : 其他参数
         * */
        return new CustomExchange(RabbitMqConst.DELAYED_EXCHANGE, "x-delayed-message", true, false, arguments);
    }

    /**创建延迟队列*/
    @Bean
    public Queue delayedQueue() {
        return new Queue(RabbitMqConst.DELAYED_QUEUE, false);
    }

    /**绑定*/
    @Bean
    public Binding BindDelayedQueueAndExchange() {
        return BindingBuilder.bind(delayedQueue()).to(delayedExchange()).with(RabbitMqConst.DELAYED_ROUTING_KEY).noargs();
    }
}
