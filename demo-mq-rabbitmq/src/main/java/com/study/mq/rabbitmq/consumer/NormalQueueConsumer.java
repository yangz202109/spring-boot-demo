package com.study.mq.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.study.mq.rabbitmq.constants.RabbitMqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * @author yangz
 * @date 2022/5/28 - 9:34
 * 普通队列·消费者
 */

@Slf4j
@Component
public class NormalQueueConsumer {

    @RabbitListener(queues = RabbitMqConst.NORMAL_QUEUE1)
    public void receiveQueue1(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间: {},收到的延迟队列的消息:{}", new Date(), msg);
    }

    /**
     *详细指定该消费监听的交换机,队列和路由key信息
     * 为什么要写的怎么详细了?
     *  因为可能出现消费端先启动,此时Mq服务器上还没有监听的交换机和队列。
     *  RabbitListener注解会监听指定的队列,如果在Mq服务器上创建交换机和队列。
     *  在这种情况下,上面只会创建一个队列
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = RabbitMqConst.NORMAL_QUEUE2, durable = "true", autoDelete = "false"),
            exchange = @Exchange(value = RabbitMqConst.NORMAL_EXCHANGE, durable = "true", autoDelete = "false"),
            key = RabbitMqConst.NORMAL_ROUTING_KEY2)
    )
    public void receiveQueue2(Message message, Channel channel) {
        String msg = new String(message.getBody());
        log.info("当前时间: {},收到的延迟队列的消息:{}", new Date(), msg);
    }


}
