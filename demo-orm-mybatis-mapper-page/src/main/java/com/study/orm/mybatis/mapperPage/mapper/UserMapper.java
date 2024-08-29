package com.study.orm.mybatis.mapperPage.mapper;

import com.study.orm.mybatis.mapperPage.entity.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * <p>
 * UserMapper
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-08 14:15
 */
@Component
public interface UserMapper extends Mapper<User>, MySqlMapper<User> {
}
