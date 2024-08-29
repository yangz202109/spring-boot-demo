package com.study.mq.kafka;

import com.study.mq.kafka.constants.KafkaConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;


@SpringBootTest
public class KafkaApplicationTests {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 测试发送消息
     */
    @Test
    public void testSend() {
        kafkaTemplate.send(KafkaConst.TOPIC_TEST, "hello,kafka...");
    }

}

