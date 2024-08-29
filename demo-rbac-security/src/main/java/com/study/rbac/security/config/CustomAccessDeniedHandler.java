package com.study.rbac.security.config;

import com.study.rbac.security.common.Status;
import com.study.rbac.security.util.ResponseUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p>
 * Security 结果处理配置
 * </p>
 * 自定义定义权限不足处理逻辑类
 *
 * @author  yangz
 * @date Created in 2018-12-07 17:31
 */

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  //登陆状态下，权限不足执行该方法
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
    ResponseUtil.renderJson(response, Status.ACCESS_DENIED, null);
  }
}
