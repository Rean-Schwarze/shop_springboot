package com.rean.shopspring.service;

import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Goods;

import java.util.List;
import java.util.Map;

public interface CartService {

    CartItem addCart(Integer id, String skuId, Integer count);

    List<CartItem> getCartList();

    void mergeCartList(List<Map<String,Object>> cartList);

    boolean deleteCartList(List<String> list);
}
