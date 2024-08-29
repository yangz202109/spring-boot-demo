package com.study.mq.rabbitmq.consumer;

import com.study.mq.rabbitmq.constants.RabbitMqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author yangz
 * @date 2022/5/31 - 11:27
 */
@Slf4j
@Component
public class WarningQueueConsumer {

    @RabbitListener(queues = RabbitMqConst.WARNING_QUEUE)
    public void receive(Message message){
        String msg = new String(message.getBody());
        log.info("报警队列收到消息 :{}",msg);
    }
}
