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
}
