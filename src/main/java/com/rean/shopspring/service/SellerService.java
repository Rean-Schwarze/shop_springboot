package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.SellerGoodsRequest;

import java.util.List;

public interface SellerService {
    Seller findByName(String name);

    List<Category> getSellCategory(int seller_id);

    List<Integer> getSellGoodsId(int seller_id, int category_id);

    void addGoods(int seller_id, SellerGoodsRequest request);
}
