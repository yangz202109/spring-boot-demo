package com.study.orm.mybatisPlus.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.study.orm.mybatisPlus.MybatisPlusApplicationTests;
import com.study.orm.mybatisPlus.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * User Service 测试
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-08 18:13
 */
@Slf4j
public class UserServiceTest extends MybatisPlusApplicationTests {
  @Autowired
  private UserService userService;

  /**
   * 测试Mybatis-Plus 新增
   */
  @Test
  public void testSave() {
    String salt = IdUtil.fastSimpleUUID();
    User testSave3 = User.builder().name("testSave3").password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave3@xkcoding.com").phoneNumber("17300000003").status(1).lastLoginTime(new DateTime()).build();
    boolean save = userService.save(testSave3);
    Assertions.assertTrue(save);
    log.debug("【测试id回显#testSave3.getId()】= {}", testSave3.getId());
  }

  /**
   * 测试Mybatis-Plus 批量新增
   */
  @Test
  public void testSaveList() {
    List<User> userList = Lists.newArrayList();
    for (int i = 4; i < 14; i++) {
      String salt = IdUtil.fastSimpleUUID();
      User user = User.builder().name("testSave" + i).password(SecureUtil.md5("123456" + salt)).salt(salt).email("testSave" + i + "@xkcoding.com").phoneNumber("1730000000" + i).status(1).lastLoginTime(new DateTime()).build();
      userList.add(user);
    }
    boolean batch = userService.saveBatch(userList);
    Assertions.assertTrue(batch);
    List<Long> ids = userList.stream().map(User::getId).collect(Collectors.toList());
    log.debug("【userList#ids】= {}", ids);
  }

  /**
   * 测试Mybatis-Plus 删除
   */
  @Test
  public void testDelete() {
    boolean remove = userService.removeById(1L);
    Assertions.assertTrue(remove);
    User byId = userService.getById(1L);
    Assertions.assertNull(byId);
  }

  /**
   * 测试Mybatis-Plus 修改
   */
  @Test
  public void testUpdate() {
    User user = userService.getById(1L);
    Assertions.assertNotNull(user);
    user.setName("MybatisPlus修改名字");
    boolean b = userService.updateById(user);
    Assertions.assertTrue(b);
    User update = userService.getById(1L);
    Assertions.assertEquals("MybatisPlus修改名字", update.getName());
    log.debug("【update】= {}", update);
  }

  /**
   * 测试Mybatis-Plus 查询单个
   */
  @Test
  public void testQueryOne() {
    User user = userService.getById(1L);
    Assertions.assertNotNull(user);
    log.debug("【user】= {}", user);
  }

  /**
   * 测试Mybatis-Plus 查询全部
   */
  @Test
  public void testQueryAll() {
    List<User> list = userService.list(new QueryWrapper<>());
    Assertions.assertTrue(CollUtil.isNotEmpty(list));
    log.debug("【list】= {}", list);
  }

  /**
   * 测试Mybatis-Plus 分页排序查询
   */
  @Test
  public void testQueryByPageAndSort() {
    initData();
    int count = (int)userService.count(new QueryWrapper<>());
    Page<User> userPage = new Page<>(1, 5);
    IPage<User> page = userService.page(userPage, new QueryWrapper<>());
    Assertions.assertEquals(5, page.getSize());
    Assertions.assertEquals(count, page.getTotal());
    log.debug("【page.getRecords()】= {}", page.getRecords());
  }

  /**
   * 测试Mybatis-Plus 自定义查询
   */
  @Test
  public void testQueryByCondition() {
    initData();
    QueryWrapper<User> wrapper = new QueryWrapper<>();
    wrapper.like("name", "Save1").or().eq("phone_number", "17300000001").orderByDesc("id");
    int count =(int) userService.count(wrapper);
    Page<User> userPage = new Page<>(1, 3);
    IPage<User> page = userService.page(userPage, wrapper);
    Assertions.assertEquals(3, page.getSize());
    Assertions.assertEquals(count, page.getTotal());
    log.debug("【page.getRecords()】= {}", page.getRecords());
  }

  /**
   * 初始化数据
   */
  private void initData() {
    testSaveList();
  }

}
