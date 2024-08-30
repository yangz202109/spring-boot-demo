package com.study.ali.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝配置类
 * @author yangz
 * @date 2024/8/30 - 11:15
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AliPayConfig {
    //商户appId
    private String appId;

    //商户秘钥
    private String privateKey;

    //商户公钥
    private String publicKey;

    //签名类型
    private String signType;

    //支付宝服务网关地址(固定)
    private String gatewayUrl;

    //参数格式(固定)
    private String format;

    //编码(固定)
    private String charset;

    /**
     * 初始化阿里支付宝客户端
     * @return AlipayClient
     */
    public AlipayClient getAlipayClient() {
        return new DefaultAlipayClient(gatewayUrl, appId, privateKey, format, charset, publicKey, signType);
    }
}
