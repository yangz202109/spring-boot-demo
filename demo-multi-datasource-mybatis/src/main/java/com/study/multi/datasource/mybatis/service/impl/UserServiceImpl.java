package com.study.multi.datasource.mybatis.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.multi.datasource.mybatis.mapper.UserMapper;
import com.study.multi.datasource.mybatis.model.User;
import com.study.multi.datasource.mybatis.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 数据服务层 实现
 * </p>
 * 使用 @DS 切换数据源
 * @author  yangz
 * @date Created in 2019-01-21 14:37
 */
@Service
@DS("slave_1")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 类上 {@code @DS("slave")} 代表默认从库，在方法上写 {@code @DS("master")} 代表默认主库
     *
     * @param user 用户
     */
    @DS("master")
    @Override
    public void addUser(User user) {
        baseMapper.insert(user);
    }
}
