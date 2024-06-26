package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Order;
import com.rean.shopspring.pojo.OrderItem;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Order postOrder(Map<String,Object> orderOption);

    Order getOrder(int id);

    int getUserOrderCounts(int user_id);

    int getUserOrderCountsByState(int user_id, int orderState);

    List<Map<String,Object>> getUserOrder(int user_id,int start,int size,int orderState);

    void updateOrderState(Integer order_id,Integer orderState);

    List<OrderItem> getOrderItemByOrderId(Integer order_id);
}
