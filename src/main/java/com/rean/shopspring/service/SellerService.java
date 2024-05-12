package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.SellerGoodsRequest;
import com.rean.shopspring.pojo.SellerOrderResponse;

import java.util.List;

public interface SellerService {
    Seller findByName(String name);

    Seller findById(Integer id);

    <T extends Category> List<T> getSellCategory(int seller_id);

    List<Integer> getSellGoodsId(int seller_id, int category_id, String type);

    Integer addGoods(int seller_id, SellerGoodsRequest request);

    void deleteGoodsFake(Integer id, Integer seller_id);

    void updateGoodsPriceAndInventory(Integer id, String price, Integer inventory, Integer goods_id, Integer seller_id);

    Integer getOrderItemCounts(Integer seller_id, Integer orderState);

    SellerOrderResponse getOrderLists(Integer seller_id, Integer orderState, Integer start, Integer pageSize);

    void updatePassword(Integer id, String newPassword);
}
