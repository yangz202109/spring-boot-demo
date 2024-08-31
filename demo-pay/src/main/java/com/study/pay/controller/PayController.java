package com.study.pay.controller;

import com.study.pay.service.AliPayService;
import com.study.pay.service.WxPayService;
import com.study.pay.vo.PayRequestVO;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yangz
 * @date 2024/8/31 - 9:15
 */
@RequestMapping("/pay")
@RestController
public class PayController {
    @Resource
    private AliPayService aliPayService;
    @Resource
    private WxPayService wxPayService;

    @PostMapping("/ali/pay")
    public String aliPay(@RequestBody PayRequestVO payRequestVO){
        return aliPayService.pay(payRequestVO);
    }


}
