package com.study.rbac.security.config;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.study.rbac.security.common.Consts;
import com.study.rbac.security.common.Status;
import com.study.rbac.security.exception.SecurityException;
import com.study.rbac.security.model.Permission;
import com.study.rbac.security.model.Role;
import com.study.rbac.security.repository.PermissionDao;
import com.study.rbac.security.repository.RoleDao;
import com.study.rbac.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义动态权限检验处理逻辑类
 * </p>
 *
 * @author yangz
 * @date Created in 2018-12-10 17:17
 */
@Component
public class CustomAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {
  @Autowired
  private RoleDao roleDao;

  @Autowired
  private PermissionDao permissionDao;

  @Autowired
  private RequestMappingHandlerMapping mapping;

  @Override
  public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
    checkRequest(object.getRequest());

    Object userInfo = authentication.get().getPrincipal();
    boolean hasPermission = false;

    if (userInfo instanceof UserDetails) {
      UserPrincipal principal = (UserPrincipal) userInfo;
      Long userId = principal.getId();

      //获取当前用户所具有的角色和权限
      List<Role> roles = roleDao.selectByUserId(userId);
      List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
      List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);

      //获取资源，前后端分离，所以过滤页面权限，只保留按钮权限
      List<Permission> btnPerms = permissions.stream()
        // 过滤页面权限
        .filter(permission -> Objects.equals(permission.getType(), Consts.BUTTON))
        // 过滤 URL 为空
        .filter(permission -> StrUtil.isNotBlank(permission.getUrl()))
        // 过滤 METHOD 为空
        .filter(permission -> StrUtil.isNotBlank(permission.getMethod())).toList();

      //校验本次请求是否有权限
      for (Permission btnPerm : btnPerms) {
        AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(btnPerm.getUrl(), btnPerm.getMethod());
        if (antPathMatcher.matches(object.getRequest())) {
          hasPermission = true;
          break;
        }
      }
    }
    return new AuthorizationDecision(hasPermission);
  }

  /**
   * 校验请求是否存在
   *
   * @param request 请求
   */
  private void checkRequest(HttpServletRequest request) {
    // 获取当前 request 的方法
    String currentMethod = request.getMethod();
    Multimap<String, String> urlMapping = allUrlMapping();

    for (String uri : urlMapping.keySet()) {
      // 通过 AntPathRequestMatcher 匹配 url
      // 可以通过 2 种方式创建 AntPathRequestMatcher
      // 1：new AntPathRequestMatcher(uri,method) 这种方式可以直接判断方法是否匹配，因为这里我们把 方法不匹配 自定义抛出，所以，我们使用第2种方式创建
      // 2：new AntPathRequestMatcher(uri) 这种方式不校验请求方法，只校验请求路径
      AntPathRequestMatcher antPathMatcher = new AntPathRequestMatcher(uri);
      if (antPathMatcher.matches(request)) {
        if (!urlMapping.get(uri).contains(currentMethod)) {
          throw new SecurityException(Status.HTTP_BAD_METHOD);
        } else {
          return;
        }
      }
    }

    throw new SecurityException(Status.REQUEST_NOT_FOUND);
  }

  /**
   * 获取 所有URL Mapping，返回格式为{"/test":["GET","POST"],"/sys":["GET","DELETE"]}
   *
   * @return {@link ArrayListMultimap} 格式的 URL Mapping
   */
  private Multimap<String, String> allUrlMapping() {
    Multimap<String, String> urlMapping = ArrayListMultimap.create();

    // 获取url与类和方法的对应信息
    Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

    handlerMethods.forEach((k, v) -> {
      // 获取当前 key 下的获取所有URL
      assert k.getPatternsCondition() != null;
      Set<String> url = k.getPatternsCondition().getPatterns();
      RequestMethodsRequestCondition method = k.getMethodsCondition();

      // 为每个URL添加所有的请求方法
      url.forEach(s -> urlMapping.putAll(s, method.getMethods().stream().map(Enum::toString).collect(Collectors.toList())));
    });

    return urlMapping;
  }

}
