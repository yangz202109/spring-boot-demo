package com.study.ali.mapper;

import com.study.ali.domain.Order;
import org.springframework.stereotype.Component;

/**
 * 订单数据库操作
 * @author yangz
 * @date 2024/8/30 - 13:42
 */
@Component
public interface OrderMapper {

    Order findById(String id);

    void updateOrderStatus(Integer status);
}
