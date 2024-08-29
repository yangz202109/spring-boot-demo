package com.study.orm.beetlsql.dao;

import com.study.orm.beetlsql.entity.User;

import org.beetl.sql.mapper.BaseMapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * UserDao
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-14 16:18
 */
@Component
public interface UserDao extends BaseMapper<User> {

}
