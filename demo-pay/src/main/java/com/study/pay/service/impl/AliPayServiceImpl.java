package com.study.pay.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePrecreateResponse;
import com.study.pay.service.AliPayService;
import com.study.pay.vo.PayRequestVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author yangz
 * @date 2024/8/31 - 9:17
 */
@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {
    @Resource
    private Config config;

    @Override
    public String pay(PayRequestVO payRequestVO) {
        Factory.setOptions(config);
        try {
            AlipayTradePrecreateResponse response = Factory.Payment.FaceToFace()
                    .preCreate("Apple iPhone11 128G", "2234567890", "5799.00");
            return response.qrCode;
        } catch (Exception e) {
            log.error("支付失败");
            return null;
        }
    }
}
