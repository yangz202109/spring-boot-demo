package com.study.task.quartz.entity.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务详情
 * </p>
 *
 * @author  yangz
 * @date Created in 2018-11-26 13:42
 */
@Data
@Accessors(chain = true)
public class JobForm {
    /**
     * 定时任务全类名
     */
    @NotBlank(message = "类名不能为空")
    private String jobClassName;
    /**
     * 任务组名
     */
    @NotBlank(message = "任务组名不能为空")
    private String jobGroupName;
    /**
     * 定时任务cron表达式
     */
    @NotBlank(message = "cron表达式不能为空")
    private String cronExpression;
}
