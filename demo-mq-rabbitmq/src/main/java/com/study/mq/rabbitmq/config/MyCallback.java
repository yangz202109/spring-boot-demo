package com.study.mq.rabbitmq.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @author yangz
 * @date 2022/5/30 - 9:55
 * 自定义回调处理类(发布确认)
 * :消息如果没有发送成功, 在生产者端进行确认,分别针对交换机和队列来确认,如果没有成功发送到消息队列服务器上,那就可以尝试重新发送.
 */
@Slf4j
@Component
public class MyCallback implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        /*注入,使用自定义的回调接口实现类*/
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 确认消息是否发送到交换机回调(无论是否成功消息都会触发执行)
     * 生产者端发送消息之后，回调confirm()方法
     *
     * @param correlationData 保存回调消息的ID及相关信息，发送消息的时候填写
     * @param b               交换机是否接收到消息(true:表示消息成功发送到了交换机 , false:表示消息没有发送到交换机   )
     * @param s               消息接收失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {
        String id = correlationData != null ? correlationData.getId() : null;
        if (b) {
            log.info("交换机成功接送到消息 id={}", id);
        } else {
            log.info("交换机没有接收到id={}的消息,由于原因:{}", id, s);
            //TDD 实际业务操作,尝试重新发送或记录错误
        }
    }

    /**
     * 确认消息是否发送到队列回调(只有当消息无法传递到不可routingKey)
     * returnedMessage()方法仅在消息没有发送到队列时调用
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息相应码: {}", returnedMessage.getReplyCode());
        log.error("消息主体: {}", returnedMessage.getMessage());
        log.error("描述: {}", returnedMessage.getReplyText());
        log.error("消息使用的交换机 : {}", returnedMessage.getExchange());
        log.error("消息使用的路由key: {}", returnedMessage.getRoutingKey());

        //TDD 实际业务操作,尝试重新发送或记录错误
    }
}
