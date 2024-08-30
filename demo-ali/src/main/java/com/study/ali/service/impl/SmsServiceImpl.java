package com.study.ali.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsResponseBody;
import com.study.ali.config.AliSmsConfig;
import com.study.ali.service.SmsService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

/**
 * 短信发送业务类
 *
 * @author yangz
 * @date 2024/8/29 - 15:43
 */

@Slf4j
@Service
public class SmsServiceImpl implements SmsService {
    @Resource
    private AliSmsConfig aliSmsConfig;

    @Override
    public void sendSms(String phone) {
        this.sendSms(phone, null);
    }

    @Override
    public void sendSms(String phone, String code) {
        if (StrUtil.isEmpty(phone)) {
            log.error("手机号不能为空");
            return;
        }
        if (StrUtil.isEmpty(code)) {
            //生成4位数的code
            code = RandomUtil.randomNumbers(4);
        }
        //可以在发送之前使用Redis来限制发送次数和频率。

        try {
            SendSmsResponseBody responseBody = sendAliSms(phone, code);
            if (responseBody.getCode() != null && responseBody.getCode().equals("OK")) {
                log.info("短信发送成功");
            } else {
                log.error("短信发送失败:{}", responseBody.getMessage());
            }
        } catch (Exception e) {
            log.info("手机号:{},短信发送失败:{}", phone, e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 发送阿里云短信
     *
     * @param phone   手机号
     * @param captcha 验证码
     * @return SendSmsResponseBody
     */
    private SendSmsResponseBody sendAliSms(String phone, String captcha) throws Exception {
        //初始化Client对象
        Client client = aliSmsConfig.createClient();

        /*
         * 短信模板: 您的验证码${code}，该验证码5分钟内有效，请勿泄露给他人！
         * 构建模板参数(根据你阿里云上配置的 ↑)  短信模板变量对应的实际值TemplateParam
         * 参考: https://help.aliyun.com/zh/sms/user-guide/verification-code-template-specifications?spm=a2c4g.11186623.0.0.339440ee7ozmAx
         */
        Map<String, String> params = new HashMap<>();
        params.put("code", captcha);

        //构建请求参数
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(aliSmsConfig.getSignName())
                .setTemplateCode(aliSmsConfig.getTemplateCode())
                .setTemplateParam(JSONUtil.toJsonStr(params));
        //发送短信
        SendSmsResponse sendSmsResponse = client.sendSms(sendSmsRequest);

        log.info("手机号:{},短信发送完成:{}", phone, JSONUtil.toJsonStr(sendSmsResponse.body));

        return sendSmsResponse.body;
    }

    //可以接入其他平台短信服务
}
