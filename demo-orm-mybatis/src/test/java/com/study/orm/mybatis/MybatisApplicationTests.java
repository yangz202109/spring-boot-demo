package com.study.orm.mybatis;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.study.orm.mybatis.entity.User;
import com.study.orm.mybatis.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

@Slf4j
@SpringBootTest
public class MybatisApplicationTests {

    @Autowired
    private UserMapper userMapper;

    /**
     * 测试查询所有
     */
    @Test
    public void selectAllUser() {
        List<User> userList = userMapper.selectAllUser();
        Assertions.assertTrue(CollUtil.isNotEmpty(userList));
        log.debug("【userList】= {}", userList);
    }

    /**
     * 测试根据主键查询单个
     */
    @Test
    public void selectUserById() {
        User user = userMapper.selectUserById(1L);
        Assertions.assertNotNull(user);
        log.debug("【user】= {}", user);
    }

    /**
     * 测试保存
     */
    @Test
    public void saveUser() {
        String salt = IdUtil.fastSimpleUUID();
        User user = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com").phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).createTime(new DateTime()).lastUpdateTime(new DateTime()).build();
        int i = userMapper.saveUser(user);
        Assertions.assertEquals(1, i);
    }

    /**
     * 测试根据主键删除
     */
    @Test
    public void deleteById() {
        int i = userMapper.deleteById(1L);
        Assertions.assertEquals(1, i);
    }
}
