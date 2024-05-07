package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private Double price;

    private Integer brandId;
}
