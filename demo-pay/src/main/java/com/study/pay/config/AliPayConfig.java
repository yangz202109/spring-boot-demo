package com.study.pay.config;

import com.alipay.easysdk.kernel.Config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yangz
 * @date 2024/8/31 - 9:10
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "alipay.easy")
@Configuration
public class AliPayConfig {

    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求网关
     */
    private String gatewayHost;
    /**
     * 签名类型
     */
    private String signType;
    /**
     * 应用ID（来自支付宝申请）
     */
    private String appId;
    /**
     * 应用秘钥
     */
    private String merchantPrivateKey;
    /**
     * 支付宝公钥
     */
    private String alipayPublicKey;
    /**
     * 支付结果异步通知的地址
     */
    private String notifyUrl;
    /**
     * 设施AES秘钥
     */
    private String encryptKey;

    @Bean
    public Config config() {
        Config config = new Config();
        config.protocol = protocol;
        config.gatewayHost = gatewayHost;
        config.signType = signType;
        config.appId = appId;
        config.merchantPrivateKey = merchantPrivateKey;
        config.alipayPublicKey = alipayPublicKey;
        config.notifyUrl = notifyUrl;
        config.encryptKey = "";
        return config;
    }

}
