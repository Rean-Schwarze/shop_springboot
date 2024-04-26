package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;

import java.util.List;

public interface SellerService {
    Seller findByName(String name);

    List<Category> getSellCategory(int seller_id);
}
