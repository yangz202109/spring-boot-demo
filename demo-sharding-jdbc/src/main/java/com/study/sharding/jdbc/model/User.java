package com.study.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author  yangz
 * @date Created in 2024-08-26 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_user")
public class User {
    private Long id;
    private String username;
    private Integer age;
    private LocalDateTime createTime;
}
