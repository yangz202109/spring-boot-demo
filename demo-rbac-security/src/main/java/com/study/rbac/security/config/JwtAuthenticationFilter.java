package com.study.rbac.security.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Sets;
import com.study.rbac.security.common.Status;
import com.study.rbac.security.exception.SecurityException;
import com.study.rbac.security.service.CustomUserDetailsService;
import com.study.rbac.security.util.JwtUtil;
import com.study.rbac.security.util.ResponseUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Set;


/**
 * <p>
 * Jwt token认证过滤器
 * </p>
 *
 * @author yangz
 * @date Created in 2018-12-10 15:15
 */

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private CustomConfig customConfig;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    // Url白名单,正常放过
    if (checkIgnores(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    //校验token
    String jwt = jwtUtil.getJwtFromRequest(request);
    if (StrUtil.isNotBlank(jwt)) {
      try {
        //从token中获取用户名称
        String username = jwtUtil.getUsernameFromJWT(jwt);
        //根据名称获取用户信息
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        //认证完成放入上下文对象中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
      } catch (SecurityException e) {
        ResponseUtil.renderJson(response, e);
      }
    } else {
      ResponseUtil.renderJson(response, Status.UNAUTHORIZED, null);
    }

  }

  /**
   * 请求是否不需要进行权限拦截
   *
   * @param request 当前请求
   * @return true - 忽略，false - 不忽略
   */
  private boolean checkIgnores(HttpServletRequest request) {
    String method = request.getMethod();

    if (StrUtil.isEmpty(method)) {
      method = "GET";
    }

    Set<String> ignores = Sets.newHashSet();

    switch (method) {
      case "GET":
        ignores.addAll(customConfig.getIgnores().getGet());
        break;
      case "PUT":
        ignores.addAll(customConfig.getIgnores().getPut());
        break;
      case "HEAD":
        ignores.addAll(customConfig.getIgnores().getHead());
        break;
      case "POST":
        ignores.addAll(customConfig.getIgnores().getPost());
        break;
      case "PATCH":
        ignores.addAll(customConfig.getIgnores().getPatch());
        break;
      case "TRACE":
        ignores.addAll(customConfig.getIgnores().getTrace());
        break;
      case "DELETE":
        ignores.addAll(customConfig.getIgnores().getDelete());
        break;
      case "OPTIONS":
        ignores.addAll(customConfig.getIgnores().getOptions());
        break;
      default:
        break;
    }

    ignores.addAll(customConfig.getIgnores().getPattern());

    if (CollUtil.isNotEmpty(ignores)) {
      for (String ignore : ignores) {
        AntPathRequestMatcher matcher = new AntPathRequestMatcher(ignore, method);
        if (matcher.matches(request)) {
          return true;
        }
      }
    }
    return false;
  }

}
