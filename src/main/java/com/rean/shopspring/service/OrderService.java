package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Order;

import java.util.Map;

public interface OrderService {
    Order postOrder(Map<String,Object> orderOption);

    Order getOrder(int id);
}
