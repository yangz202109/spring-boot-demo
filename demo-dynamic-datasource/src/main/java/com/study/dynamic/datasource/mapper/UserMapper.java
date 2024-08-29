package com.study.dynamic.datasource.mapper;

import com.study.dynamic.datasource.config.MyMapper;
import com.study.dynamic.datasource.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户 Mapper
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-09-04 16:49
 */
@Mapper
public interface UserMapper extends MyMapper<User> {
}
