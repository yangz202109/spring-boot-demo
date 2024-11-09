package com.study.async.task;

import com.study.async.AsyncApplicationTests;
import com.study.async.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.concurrent.CompletableFuture;

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
    public void test1() {
        taskService.syncTask();
        log.info("测试方法执行完毕");
    }

    @Test
    public void test() throws Exception {
        long start = System.currentTimeMillis();

        CompletableFuture<String> taskGetResult = taskService.syncTaskGetResult();
        try {
            //阻塞等待结果
            String result = taskGetResult.get();
            long end = System.currentTimeMillis();
            log.info("任务全部完成,总耗时：{} 毫秒,result:{}", (end - start), result);
        } catch (InterruptedException e) {
            log.error("执行异步任务错误,msg:{}", e.getMessage());
        }
    }
}
