package com.study.sharding.jdbc.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * 订单表
 * </p>
 *
 * @author  yangz
 * @date Created in 2024-08-26 13:38
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName(value = "t_order")
public class Order {
    /**
     * 主键
     */
    @TableId
    private Long id;
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单id
     */
    private Long orderId;
    /**
     * 备注
     */
    private String remark;
}
