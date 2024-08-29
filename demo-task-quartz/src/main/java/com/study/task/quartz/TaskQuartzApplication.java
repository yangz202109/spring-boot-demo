package com.study.task.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * <p>
 * 启动器
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-23 20:33
 */
@MapperScan(basePackages = {"com.study.task.quartz.mapper"})
@SpringBootApplication
public class TaskQuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskQuartzApplication.class, args);
    }
}
