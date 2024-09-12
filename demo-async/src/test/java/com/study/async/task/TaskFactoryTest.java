package com.study.async.task;

import com.study.async.AsyncApplicationTests;
import com.study.async.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>
 * 测试任务
 * </p>
 *
 * @author yangz
 * @date Created in 2018-12-29 10:49
 */
@Slf4j
public class TaskFactoryTest extends AsyncApplicationTests {
    @Autowired
    private TaskService taskService;

    @Test
    public void test() {
        taskService.syncTask();
        log.info("测试方法执行完毕");
    }

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();
     
        Future<String> task = taskService.syncTaskGetResult();
        while(true) {
            if(task.isDone() ) {
                // 任务都调用完成，退出循环等待
                break;
            }
            Thread.sleep(1000);
        }
        long end = System.currentTimeMillis();
        log.info("任务全部完成，总耗时：{} "毫秒"" , (end - start));
    }
}
