package com.study.rbac.security.config;

import com.study.rbac.security.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <p>
 * Security 配置
 * </p>
 * 新版不需要继承WebSecurityConfigurerAdapter
 *
 * @author yangz
 * @date Created in 2018-12-07 16:46
 */

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties(CustomConfig.class)
public class SecurityConfig {
  @Autowired
  private CustomConfig customConfig;

  @Autowired
  private CustomAccessDeniedHandler accessDeniedHandler;

  @Autowired
  private CustomAuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private CustomAuthorizationManager authorizationManager;

  @Autowired
  private CustomUserDetailsService customUserDetailsService;

  @Autowired
  private JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  //加密器
  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(customUserDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }

  /**
   * 核心配置
   * spring security的核心过滤器链
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      //跨域
      .cors(Customizer.withDefaults())
      // 前后端分离,关闭csrf
      .csrf(AbstractHttpConfigurer::disable)
      // 前后端分离架构,使用了JWT 禁用session
      .sessionManagement(sessionManagementConfigurer -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      // 访问异常处理
      .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler))
      // 未授权异常处理
      .exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer.authenticationEntryPoint(authenticationEntryPoint))
      // 禁用缓存
      .headers(securityHeadersConfigurer -> {
        securityHeadersConfigurer.cacheControl(HeadersConfigurer.CacheControlConfig::disable);
        securityHeadersConfigurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
      })
      // 登录行为由自己实现,参考 AuthController#login
      .formLogin(AbstractHttpConfigurer::disable)
      // 登出行为由自己实现,参考 AuthController#logout
      .logout(AbstractHttpConfigurer::disable)
      .httpBasic(AbstractHttpConfigurer::disable)
      //请求认证
      .authorizeHttpRequests(requestMatcherRegistry -> requestMatcherRegistry
        // 其他请求全部要认证
        .anyRequest().authenticated()
        // RBAC 动态 url 认证
        .anyRequest()
        .access(authorizationManager)
      );

    // 添加自定义 JWT认证过滤器 放在 springSecurity认证过滤器的前面
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * 忽略权限校验
   */
  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    IgnoreConfig ignores = customConfig.getIgnores();

    //放行所有不需要登录就可以访问的请求
    return web -> web.ignoring()
      // 忽略 GET
      .requestMatchers(HttpMethod.GET, ignores.getGet().toArray(new String[0]))
      // 忽略 POST
      .requestMatchers(HttpMethod.POST, ignores.getPost().toArray(new String[0]))
      // 忽略 DELETE
      .requestMatchers(HttpMethod.DELETE, ignores.getDelete().toArray(new String[0]))
      // 忽略 PUT
      .requestMatchers(HttpMethod.PUT, ignores.getPut().toArray(new String[0]))
      // 忽略 OPTIONS
      .requestMatchers(HttpMethod.OPTIONS, ignores.getOptions().toArray(new String[0]))
      // 忽略 TRACE
      .requestMatchers(HttpMethod.TRACE, ignores.getTrace().toArray(new String[0]))
      // 按照请求格式忽略
      .requestMatchers(ignores.getPattern().toArray(new String[0]));
  }
}
