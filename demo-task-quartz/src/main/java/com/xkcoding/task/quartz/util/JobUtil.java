package com.xkcoding.task.quartz.util;

import com.xkcoding.task.quartz.job.base.BaseJob;
import java.lang.reflect.Constructor;

/**
 * <p>
 * 定时任务反射工具类
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-26 13:33
 */
public class JobUtil {
  /**
   * 根据全类名获取Job实例
   *
   * @param classname Job全类名
   * @return {@link BaseJob} 实例
   * @throws Exception 泛型获取异常
   */
  public static BaseJob getClass(String classname) throws Exception {
    Class<?> clazz = Class.forName(classname);
    Constructor<?> constructor = clazz.getConstructor(); // 获取无参构造函数
    return (BaseJob) constructor.newInstance();
  }
}
