package com.study.orm.mybatisPlus.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.study.orm.mybatisPlus.entity.User;
import com.study.orm.mybatisPlus.mapper.UserMapper;
import com.study.orm.mybatisPlus.service.UserService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * User Service
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-08 18:10
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
