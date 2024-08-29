package com.study.task.xxl.job.task;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 测试定时任务
 * </p>
 *
 * @author  yangz
 * @date Created in 2019-08-07 10:15
 */
@Slf4j
@Component
public class DemoTask extends IJobHandler {

  /**
   * execute handler, invoked when executor receives a scheduling request
   *
   * @throws Exception 任务异常
   */
  @XxlJob("demoTask")
  @Override
  public void execute() throws Exception {
    // 通过 "XxlJobHelper.getJobParam" 获取任务参数
    String jobParam = XxlJobHelper.getJobParam();

    XxlJobHelper.log("demo task run at : {}", DateUtil.now());
    XxlJobHelper.handleSuccess();           // 设置任务结果
  }
}
