package com.study.ali.service;

/**
 * @author yangz
 * @date 2024/8/29 - 15:42
 */
public interface AliSmsService {

    void sendSms(String phone);

    void sendSms(String phone, String code);
}
