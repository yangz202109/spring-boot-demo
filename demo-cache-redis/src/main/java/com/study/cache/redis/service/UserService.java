package com.study.cache.redis.service;

import com.study.cache.redis.entity.User;

/**
 * <p>
 * UserService
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-15 16:45
 */
public interface UserService {
    /**
     * 保存或修改用户
     *
     * @param user 用户对象
     * @return 操作结果
     */
    User saveOrUpdate(User user);

    /**
     * 获取用户
     *
     * @param id key值
     * @return 返回结果
     */
    User get(Long id);

    /**
     * 删除
     *
     * @param id key值
     */
    void delete(Long id);
}
