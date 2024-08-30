package com.study.ali.controller;

import com.study.ali.service.AliPayService;
import com.study.ali.vo.PayRequestVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

/**
 * @author yangz
 * @date 2024/8/30 - 10:27
 */
@RequestMapping("/pay")
@RestController
public class PayController {

    @Resource
    private AliPayService aliPayService;

    /**
     * ali支付
     *
     * @param payRequestVO 支付请求VO
     *  @return 跳转支付宝页面数据
     */
    @PostMapping("/ali/pay")
    public String aliPay(@RequestBody PayRequestVO payRequestVO) {
        return aliPayService.pay(payRequestVO);
    }


    /**
     * ali支付同步通知
     */
    @GetMapping("/ali/return")
    public String handleReturn(HttpServletRequest request) {
        // 处理同步返回
        boolean result = aliPayService.payReturn(request);
        if (result) {
            // 支付成功处理逻辑
            return "支付成功";
        } else {
            // 支付失败处理逻辑
            return "支付失败";
        }
    }

    /**
     * ali支付异步通知
     * 返回参数含义:
     * 返回success给支付宝,表示消息我已收到,不用重调
     * 返回fail给支付宝,表示消息我没收到,请重试
     */
    @PostMapping("/ali/notify")
    public String handleNotify(HttpServletRequest request) {
        // 处理异步通知
        boolean result = aliPayService.payNotify(request);
        if (result) {
            //必须返回success,否则支付宝会重复发送通知
            return "success";
        } else {
            return "fail";
        }
    }

    /**
     * ali支付退款
     *
     * @param payRequestVO 支付请求VO
     * @return 提示
     */
    @PostMapping("/aliRefund")
    public String aliRefund(@RequestBody PayRequestVO payRequestVO) {
        boolean isSuccess = aliPayService.aliRefund(payRequestVO);
        return isSuccess ? "退款成功" : "退款失败";
    }

}
