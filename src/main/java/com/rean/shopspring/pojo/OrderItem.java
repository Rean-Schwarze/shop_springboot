package com.rean.shopspring.pojo;

import lombok.Data;

@Data
public class OrderItem {
    private String skuId;
    private int user_id;
    private int order_id;
    private int goods_id;
    private int count;
    private double price;
}
