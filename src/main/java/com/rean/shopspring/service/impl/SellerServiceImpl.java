package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.SellerMapper;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.Seller_categories;
import com.rean.shopspring.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Override
    public Seller findByName(String name){
        return sellerMapper.findByName(name);
    }

    // 获取负责的分类
    @Override
    public List<Category> getSellCategory(int seller_id){
        List<Category> categoryList=new ArrayList<>();
        Set<Integer> categoryIdSet=new HashSet<>();
        Map<Integer,List<Integer>> categoryIdMap=new HashMap<>();

        List<Seller_categories> seller_categories=sellerMapper.getSellCategory(seller_id);

//        先用Map获取负责的父、子分类id
        for(Seller_categories seller_category : seller_categories){
            int category_id=seller_category.getCategoryId();
            categoryIdSet.add(category_id);

            if(seller_category.isAllSub()){
                categoryIdMap.put(category_id,categoryMapper.getSubCategoryIdByParentId(category_id));
            }
            else{
                if(categoryIdMap.get(category_id)!=null){
                    categoryIdMap.get(category_id).add(seller_category.getSubCategoryId());
                }
                else{
                    List<Integer> tmp=new ArrayList<>();
                    tmp.add(seller_category.getSubCategoryId());
                    categoryIdMap.put(category_id,tmp);
                }
            }
        }

//        遍历父分类，添加子分类
        for(Integer category_id:categoryIdSet){
            Category category=categoryMapper.getCategoryById(category_id);
            List<Category> children=new ArrayList<>();
            for(Integer sub_id:categoryIdMap.get(category_id)){
                children.add(categoryMapper.getSubCategoryById(sub_id));
            }
            category.setChildren(children);
            categoryList.add(category);
        }

        return categoryList;
    }

    // 获取负责分类下的商品id列表
    public List<Integer> getSellGoodsId(int seller_id, int category_id){
        // 首先获取品牌id
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);
        return goodsMapper.getGoodsIdByBrandAndCategory(brand_id,category_id);
    }
}
