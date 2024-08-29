package com.study.ali.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 阿里云sms配置类
 *
 * @author yangz
 * @date 2024/8/29 - 15:05
 */
@Data
@Component
@ConfigurationProperties(prefix = "sms.aliyun")
public class AliYunSmsConfig {

    //阿里云访问密钥id
    private String accessKeyId;
    //阿里云访问密钥Secret
    private String accessKeySecret;
    //短信签名名称
    private String signName;
    //短信模板Code
    private String templateCode;
    private String endpoint;


    /**
     * 使用AK&SK初始化账号Client
     *
     * @return Client
     */
    public Client createClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret);
        // 配置 Endpoint
        config.endpoint = endpoint;
        return new Client(config);
    }
}
