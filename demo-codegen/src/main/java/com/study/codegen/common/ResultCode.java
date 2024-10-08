package com.study.codegen.common;

import lombok.Getter;

/**
 * <p>
 * 通用状态枚举
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-03-22 10:13
 */
@Getter
public enum ResultCode implements IResultCode {
    /**
     * 成功
     */
    OK(200, "成功"),
    /**
     * 失败
     */
    ERROR(500, "失败");

    /**
     * 返回码
     */
    private Integer code;

    /**
     * 返回消息
     */
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
