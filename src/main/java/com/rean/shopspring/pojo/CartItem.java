package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @NotEmpty
    private String id; // 商品id
    private String price;
    private Integer count;
    private String skuId;
    private String name;
    private String attrsText; // 商品属性
    private List<String> specs;
    private String picture;
    private String nowPrice;
    private String nowOriginalPrice;
    private boolean selected;
    private Integer stock;
    private boolean isEffective;
    private boolean isCollect;
    private Integer postFee;
    private String payPrice;
    private String totalPrice;
    private String totalPayPrice;
    private int user_id;
}
