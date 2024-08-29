package com.study.async.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;

/**
 * 自定义任务线程池配置类
 */
@EnableAsync //开启异步支持
@Configuration
public class TaskPoolConfig {

    @Bean(name = "taskExecutor")
    public Executor customAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //核心线程数量
        executor.setCorePoolSize(5);
        //最大线程数量
        executor.setMaxPoolSize(10);
        //队列容量
        executor.setQueueCapacity(20);
        //线程的名称前缀
        executor.setThreadNamePrefix("taskExecutor-");
        //存活时间(非核心线程的)
        executor.setKeepAliveSeconds(60);
        //是否得到所有任务执行完毕后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
}
