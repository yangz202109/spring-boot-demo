package com.study.async;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AsyncApplicationTests {
    @Resouce
    private TaskService taskService;

    @Test
    public void contextLoads() {
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
        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
    }
}

