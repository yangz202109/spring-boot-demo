package com.study.ali.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.study.ali.config.AliYunSmsConfig;
import com.study.ali.service.AliSmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangz
 * @date 2024/8/29 - 15:43
 */

@Slf4j
@Service
public class AliSmsServiceImpl implements AliSmsService {
    @Resource
    private AliYunSmsConfig aliYunSmsConfig;


    @Override
    public void sendSms(String phone) {
        this.sendSms(phone, null);
    }

    @Override
    public void sendSms(String phone, String code) {
        if (StrUtil.isEmpty(phone)){
            log.error("手机号不能为空");
            return;
        }

        if (StrUtil.isEmpty(code)){
            //生成4位数的code
            code = RandomUtil.randomNumbers(4);
        }
        //可以在发送之前,限制发送次数。

        try {
            sendAliSms(phone,code);
        } catch (Exception e) {
            log.info("手机号:{},短信发送失败:{}", phone,e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送阿里云短信
     *
     * @param phone   手机号
     * @param captcha  验证码
     */
    private void sendAliSms(String phone, String captcha) throws Exception {
        //初始化Client对象
        Client client = aliYunSmsConfig.createClient();

        /*
         * 您的验证码${code}，该验证码5分钟内有效，请勿泄露给他人！
         * 构建模板参数(根据你阿里云上配置的 ↑)  短信模板变量对应的实际值TemplateParam
         * 参考: https://help.aliyun.com/zh/sms/user-guide/verification-code-template-specifications?spm=a2c4g.11186623.0.0.339440ee7ozmAx
         */
        Map<String, String> params = new HashMap<>();
        params.put("code", captcha);

        //构建请求参数
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(aliYunSmsConfig.getSignName())
                .setTemplateCode(aliYunSmsConfig.getTemplateCode())
                .setTemplateParam(JSONUtil.toJsonStr(params));
        //发送短信
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        log.info("手机号:{},短信发送完成:{}", phone, JSONUtil.toJsonStr(sendSmsResponse.body));
    }

}
