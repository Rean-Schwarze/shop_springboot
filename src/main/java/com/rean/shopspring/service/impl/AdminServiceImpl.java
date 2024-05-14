package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.AdminMapper;
import com.rean.shopspring.mapper.SellerMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.AdminService;
import com.rean.shopspring.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public Admin findByName(String name) {return adminMapper.findByName(name);}

    @Override
    public Integer getSellerCount(Integer brand_id, Boolean isValid){
        if(brand_id==0){
            return adminMapper.getSellerCountAll(isValid);
        }
        else{
            return adminMapper.getSellerCountByBrandId(brand_id,isValid);
        }
    }

    // 获取销售人员列表（除分类）
    @Override
    public List<AdminSellerResponse> getSellerLists(Integer brand_id, Integer start, Integer pageSize, Boolean isValid){
        List<Seller> sellerList;
        if(brand_id==0){
            sellerList=adminMapper.getSellerAll(start,pageSize,isValid);
        }
        else{
            sellerList=adminMapper.getSellerByBrandId(brand_id,start,pageSize,isValid);
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

    @Override
    public Integer sellerRegister(SellerRegisterRequest request){
        // 加密密码
        String md5String= Md5Util.getMD5String(request.getPassword());
        request.setMd5Password(md5String);
        sellerMapper.sellerRegister(request);
        Integer seller_id=request.getId();

        // 绑定负责分类
        if(request.getCategory()!=null){
            for(SellerCategory category:request.getCategory()){
                Seller_categories seller_categories=new Seller_categories(seller_id, category.getId(),
                        null, category.isAllSub());
                if(!category.isAllSub()){
                    for(Category sub:category.getChildren()){
                        seller_categories.setSubCategoryId(sub.getId());
                        sellerMapper.bindCategory(seller_categories);
                    }
                }
                else{
                    sellerMapper.bindCategory(seller_categories);
                }
            }
        }

        return seller_id;
    }

    // 删除销售人员（伪）
    @Override
    public void deleteSellerFake(Integer id){
        adminMapper.deleteSellerFake(id);
    }

    // 获取品牌
    @Override
    public List<Brand> getBrandsAll(){
        return adminMapper.getBrandsAll();
    }
}
