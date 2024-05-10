package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Admin;
import com.rean.shopspring.pojo.AdminSellerResponse;
import com.rean.shopspring.pojo.SellerRegisterRequest;

import java.util.List;

public interface AdminService {
    Admin findByName(String name);

    Integer getSellerCount(Integer brand_id);

    List<AdminSellerResponse> getSellerLists(Integer brand_id, Integer start, Integer pageSize);

    Integer sellerRegister(SellerRegisterRequest request);
}
