package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Integer id;
    private Integer skuId;
    private Integer userId;
    private Integer orderId;
    private Integer goodsId;
    private Integer count;
    private Integer payMoney;
    private Double price;

    private Integer brandId;
    private Integer orderState; // 1为待付款、2为待发货、3为待收货、4为待评价、5为已完成、6为已取消
    private Timestamp createTime;
}
