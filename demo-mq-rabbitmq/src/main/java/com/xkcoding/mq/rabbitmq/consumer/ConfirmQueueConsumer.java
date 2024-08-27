package com.xkcoding.mq.rabbitmq.consumer;

import com.rabbitmq.client.Channel;
import com.xkcoding.mq.rabbitmq.constants.RabbitMqConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author yangz
 * @date 2022/5/30 - 9:27
 * 发布确认客服端: 消息手动确认
 * 步骤1: 把消息确认模式改为手动确认(配置文件中)
 * 步骤2: 调用Channel对象的方法返回信息
 * - ACK：Acknowledgement，表示消息处理成功
 * - NACK：Negative Acknowledgement，表示消息处理失败
 * - Reject：拒绝，同样表示消息处理失败

 * 后续操作
 * - requeue为true：重新放回队列，重新投递，再次尝试
 * - requeue为false：不放回队列，不重新投递
 */
@Slf4j
@Component
public class ConfirmQueueConsumer {

    @RabbitListener(queues = RabbitMqConst.CONFIRM_QUEUE)
    public void receive(Message message, Channel channel) throws IOException {
        // 1、获取当前消息的 deliveryTag 值是消息的唯一标识,查找具体某一条消息的依据
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String msg = new String(message.getBody());
            log.info("接收到消息：{}", msg);
            //2. 业务处理 模拟异常
            // System.out.println(10 / 0);

            // 3、给 RabbitMQ 服务器返回 ACK 确认信息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            // 4、获取信息，看当前消息是否曾经被投递过
            Boolean redelivered = message.getMessageProperties().getRedelivered();

            if (!redelivered) {
                // 5、如果没有被投递过，那就重新放回队列，重新投递，再试一次
                channel.basicNack(deliveryTag, false, true);
            } else {
                // 6、如果已经被投递过，且这一次仍然进入了 catch 块，那么返回拒绝且不再放回队列
                channel.basicReject(deliveryTag, false);
            }
        }
    }
}
