package com.study.ali.service;

import com.study.ali.vo.PayRequestVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author yangz
 * @date 2024/8/30 - 10:14
 */
public interface AliPayService {

    String pay(PayRequestVO payRequestVO);

    boolean payReturn(HttpServletRequest request);

    boolean payNotify(HttpServletRequest request);

    boolean aliRefund(PayRequestVO payRequestVO);

}
