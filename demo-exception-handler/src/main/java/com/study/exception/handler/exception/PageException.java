package com.study.exception.handler.exception;

import com.study.exception.handler.constant.Status;
import lombok.Getter;

/**
 * <p>
 * 页面异常
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-10-02 21:18
 */
@Getter
public class PageException extends BaseException {

    public PageException(Status status) {
        super(status);
    }

    public PageException(Integer code, String message) {
        super(code, message);
    }
}
