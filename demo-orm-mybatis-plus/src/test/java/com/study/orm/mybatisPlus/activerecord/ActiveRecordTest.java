package com.study.orm.mybatisPlus.activerecord;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.study.orm.mybatisPlus.MybatisPlusApplicationTests;
import com.study.orm.mybatisPlus.entity.Role;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * <p>
 * Role
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-09-14 14:19
 */
@Slf4j
public class ActiveRecordTest extends MybatisPlusApplicationTests {
  /**
   * 测试 ActiveRecord 插入数据
   */
  @Test
  public void testActiveRecordInsert() {
    Role role = new Role();
    role.setName("VIP");
    Assertions.assertTrue(role.insert());
    // 成功直接拿会写的 ID
    log.debug("【role】= {}", role);
  }

  /**
   * 测试 ActiveRecord 更新数据
   */
  @Test
  public void testActiveRecordUpdate() {
    Assertions.assertTrue(new Role().setId(1L).setName("管理员-1").updateById());
    Assertions.assertTrue(new Role().update(new UpdateWrapper<Role>().lambda().set(Role::getName, "普通用户-1").eq(Role::getId, 2)));
  }

  /**
   * 测试 ActiveRecord 查询数据
   */
  @Test
  public void testActiveRecordSelect() {
    Assertions.assertEquals("管理员", new Role().setId(1L).selectById().getName());
    Role role = new Role().selectOne(new QueryWrapper<Role>().lambda().eq(Role::getId, 2));
    Assertions.assertEquals("普通用户", role.getName());
    List<Role> roles = new Role().selectAll();
    Assertions.assertFalse(roles.isEmpty());
    log.debug("【roles】= {}", roles);
  }
  /**
   * 测试 ActiveRecord 删除数据
   */
  @Test
  public void testActiveRecordDelete() {
    Assertions.assertTrue(new Role().setId(1L).deleteById());
    Assertions.assertTrue(new Role().delete(new QueryWrapper<Role>().lambda().eq(Role::getName, "普通用户")));
  }
}
