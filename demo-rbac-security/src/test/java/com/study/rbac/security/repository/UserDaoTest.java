package com.study.rbac.security.repository;

import com.study.rbac.security.SecurityApplicationTests;
import com.study.rbac.security.model.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * <p>
 * UserDao 测试
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-12-12 01:10
 */
@Slf4j
public class UserDaoTest extends SecurityApplicationTests {
    @Autowired
    private UserDao userDao;

    @Test
    public void findByUsernameIn() {
        List<String> usernameList = Lists.newArrayList("admin", "user");
        List<User> userList = userDao.findByUsernameIn(usernameList);
        Assertions.assertEquals(2, userList.size());
        log.info("[userList]= {}", userList);
    }
}
