package com.xkcoding.async.task;

import com.xkcoding.async.SpringBootDemoAsyncApplicationTests;
import com.xkcoding.async.service.TaskService;
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
public class TaskFactoryTest extends SpringBootDemoAsyncApplicationTests {
    @Autowired
    private TaskService taskService;

    @Test
    public void test() {
        taskService.syncTask();
        log.info("测试方法执行完毕");
    }
}
