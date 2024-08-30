package com.study.ali.vo;

import lombok.Data;

/**
 * @author yangz
 * @date 2024/8/30 - 11:07
 */
@Data
public class PayRequestVO {

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 订单总金额
     */
    private String totalAmount;

    /**
     * 订单标题
     */
    private String subject;

}
