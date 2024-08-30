package com.study.ali.domain;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 订单实体类
 * @author yangz
 * @date 2024/8/30 - 13:35
 */

@Data
public class Order {
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 订单内容/描述
     */
    private String content;

    /**
     * 下单用户id
     */
    private Long userId;

    /**
     * 订单金额
     */
    private Double amount;

    /**
     * 订单状态 0:未支付 1:已支付 2:支付失败 3:已退款
     */
    private Integer orderStatus;

    /**
     * 创建/下单时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
