package com.study.orm.jpa.repository;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.study.orm.jpa.entity.User;
import com.study.orm.jpa.JpaApplicationTests;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * jpa 测试类
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-07 14:09
 */

@Slf4j
public class UserDaoTest extends JpaApplicationTests {
  @Autowired
  private UserDao userDao;

  /**
   * 测试保存
   */
  @Test
  public void testSave() {
    String salt = IdUtil.fastSimpleUUID();
    User testSave3 = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com").phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).build();
    userDao.save(testSave3);

    Assertions.assertNotNull(testSave3.getId());
    Optional<User> byId = userDao.findById(testSave3.getId());
    Assertions.assertTrue(byId.isPresent());
    log.debug("【byId】= {}", byId.get());
  }

  /**
   * 测试删除
   */
  @Test
  public void testDelete() {
    long count = userDao.count();
    userDao.deleteById(1L);
    long left = userDao.count();
    Assertions.assertEquals(count - 1, left);
  }

  /**
   * 测试修改
   */
  @Test
  public void testUpdate() {
    userDao.findById(1L).ifPresent(user -> {
      user.setName("JPA修改名字");
      userDao.save(user);
    });
    Assertions.assertEquals("JPA修改名字", userDao.findById(1L).get().getName());
  }

  /**
   * 测试查询单个
   */
  @Test
  public void testQueryOne() {
    Optional<User> byId = userDao.findById(1L);
    Assertions.assertTrue(byId.isPresent());
    log.debug("【byId】= {}", byId.get());
  }

  /**
   * 测试查询所有
   */
  @Test
  public void testQueryAll() {
    List<User> users = userDao.findAll();
    Assertions.assertNotEquals(0, users.size());
    log.debug("【users】= {}", users);
  }

  /**
   * 测试分页排序查询
   */
  @Test
  public void testQueryPage() {
    // 初始化数据
    initData();
    // JPA分页的时候起始页是页码减1
    int currentPage = 0;
    int pageSize = 5;
    Sort sort = Sort.by(Sort.Direction.DESC, "id");
    PageRequest pageRequest = PageRequest.of(currentPage, pageSize, sort);
    Page<User> userPage = userDao.findAll(pageRequest);

    Assertions.assertEquals(5, userPage.getSize());
    Assertions.assertEquals(userDao.count(), userPage.getTotalElements());
    log.debug("【id】= {}", userPage.getContent().stream().map(User::getId).collect(Collectors.toList()));
  }

  /**
   * 初始化10条数据
   */
  private void initData() {
    List<User> userList = Lists.newArrayList();
    for (int i = 0; i < 10; i++) {
      String salt = IdUtil.fastSimpleUUID();
      int index = 3 + i;
      User user = User.builder().name("testSave" + index).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + index + "@xkcoding.com").phoneNumber("1730000000" + index).status(1).lastLoginTime(new DateTime()).build();
      userList.add(user);
    }
    userDao.saveAll(userList);
  }

}
