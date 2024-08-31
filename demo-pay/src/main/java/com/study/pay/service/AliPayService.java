package com.study.pay.service;

import com.study.pay.vo.PayRequestVO;

/**
 * @author yangz
 * @date 2024/8/31 - 9:15
 */
public interface AliPayService {
    String pay(PayRequestVO payRequestVO);
}
