package com.study.multi.datasource.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.study.multi.datasource.mybatis.model.User;

/**
 * <p>
 * 数据服务层
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-01-21 14:31
 */
public interface UserService extends IService<User> {

    /**
     * 添加 User
     *
     * @param user 用户
     */
    void addUser(User user);
}
