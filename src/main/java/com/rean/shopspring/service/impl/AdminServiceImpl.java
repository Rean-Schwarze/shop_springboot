package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.AdminMapper;
import com.rean.shopspring.pojo.Admin;
import com.rean.shopspring.pojo.AdminSellerResponse;
import com.rean.shopspring.pojo.Brand;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin findByName(String name) {return adminMapper.findByName(name);}

    @Override
    public Integer getSellerCount(Integer brand_id){
        if(brand_id==0){
            return adminMapper.getSellerCountAll();
        }
        else{
            return adminMapper.getSellerCountByBrandId(brand_id);
        }
    }

    // 获取销售人员列表（除分类）
    @Override
    public List<AdminSellerResponse> getSellerLists(Integer brand_id, Integer start, Integer pageSize){
        List<Seller> sellerList;
        if(brand_id==0){
            sellerList=adminMapper.getSellerAll(start,pageSize);
        }
        else{
            sellerList=adminMapper.getSellerByBrandId(brand_id,start,pageSize);
        }
        List<AdminSellerResponse> responses=new ArrayList<>();
        for(Seller seller:sellerList){
            AdminSellerResponse response=new AdminSellerResponse();
            response.setId(seller.getId());
            response.setName(seller.getName());
            response.setAvatar(seller.getAvatar());
            response.setMaster(seller.isMaster());
            response.setValid(seller.isValid());
            Brand brand1=adminMapper.getBrandById(seller.getBrandId());
            AdminSellerResponse.Brand brand=new AdminSellerResponse.Brand(brand1.getId(),brand1.getName());
            response.setBrand(brand);
            responses.add(response);
        }
        return responses;
    }
}
