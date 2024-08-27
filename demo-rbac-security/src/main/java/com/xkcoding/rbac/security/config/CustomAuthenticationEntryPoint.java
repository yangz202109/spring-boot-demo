package com.xkcoding.rbac.security.config;

import com.xkcoding.rbac.security.common.Status;
import com.xkcoding.rbac.security.util.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.Serializable;

/**
 * <p>
 * Security 结果处理配置
 * </p>
 * 自定义未登录情况处理逻辑类
 *
 * @author  yangz
 * @date Created in 2018-12-07 17:31
 */

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {
  //验证为未登陆状态会进入此方法，认证错误
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
    ResponseUtil.renderJson(response, Status.UNAUTHORIZED, null);
  }
}
