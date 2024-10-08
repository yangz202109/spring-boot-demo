package com.study.mq.rabbitmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 测试消息体
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-29 16:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageStruct implements Serializable {
    @Serial
    private static final long serialVersionUID = 392365881428311040L;

    private String message;
}
