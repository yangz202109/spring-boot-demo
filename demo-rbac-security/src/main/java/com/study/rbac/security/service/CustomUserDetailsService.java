package com.study.rbac.security.service;

import com.study.rbac.security.model.Permission;
import com.study.rbac.security.model.Role;
import com.study.rbac.security.model.User;
import com.study.rbac.security.repository.PermissionDao;
import com.study.rbac.security.repository.RoleDao;
import com.study.rbac.security.repository.UserDao;
import com.study.rbac.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 自定义UserDetails查询
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-10 10:29
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmailOrPhone) throws UsernameNotFoundException {
        User user = userDao.findByUsernameOrEmailOrPhone(usernameOrEmailOrPhone, usernameOrEmailOrPhone, usernameOrEmailOrPhone).orElseThrow(() -> new UsernameNotFoundException("未找到用户信息 : " + usernameOrEmailOrPhone));
        List<Role> roles = roleDao.selectByUserId(user.getId());
        List<Long> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
        List<Permission> permissions = permissionDao.selectByRoleIdList(roleIds);
        return UserPrincipal.create(user, roles, permissions);
    }
}
