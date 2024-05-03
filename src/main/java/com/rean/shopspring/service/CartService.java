package com.rean.shopspring.service;

import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Goods;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartItem addCart(Integer id, Integer skuId, Integer count);

//    获取用户购物车商品
    List<CartItem> getCartList();
    List<CartItem> getCartList(List<Integer> skus);

//    获取用户购物车中特定商品（给定sku的id）
//    List<CartItem> getCartListBySkuId(List<String> skus);
    void mergeCartList(List<Map<String,Object>> cartList);

    boolean deleteCartList(List<Integer> list);
}
