package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.SellerMapper;
import com.rean.shopspring.pojo.*;
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

    // 添加商品
    public boolean addGoods(int seller_id, SellerGoodsRequest request){
        // 首先获取品牌id
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);

        int id=goodsMapper.addGoods(request.getName(), request.getDesc(),brand_id,request.getCategory(),
                request.getSubCategory(),request.getSubCategory2(),
                request.isPreSale(), request.isNew(), request.isOnSale(),
                request.getSvdName(),request.getPubTime(),seller_id);

        // 添加规格
        Map<String,Map<String,Integer>> specs_map=new HashMap<>();
        for(Specs specs:request.getSpecs()){
            int specs_id=goodsMapper.addSpecs(specs.getName(),id);
            // 添加具体规格
            Map<String,Integer> specs_values_map=new HashMap<>();
            for(Spec_values spec_values:specs.getValues()){
                int value_id= goodsMapper.addSpecsValues(spec_values.getName(),spec_values.getPicture(),spec_values.getDesc(),specs_id,id);
                specs_values_map.put(spec_values.getName(),value_id);
            }
            specs_map.put(specs.getName(),specs_values_map);
        }

        // 添加sku
        int minPrice=Integer.MAX_VALUE;
        for(Sku sku: request.getSkus()){
            // 计算最低价格
            minPrice=Integer.min(Integer.parseInt(sku.getPrice()),minPrice);
            List<Spec_sku> spec_skus=sku.getSpecs();
            List<Integer> values=new ArrayList<>();
            for(Spec_sku spec_sku:spec_skus){
                values.add(specs_map.get(spec_sku.getName()).get(spec_sku.getValueName()));
            }
            if(values.size()==1){
                goodsMapper.addSkuSingle(sku.getPrice(),sku.getInventory(),values.get(0),id);
            }
            else{
                goodsMapper.addSkuDouble(sku.getPrice(),sku.getInventory(),values.get(0),values.get(1),id);
            }
        }

        // 修改goods价格
        goodsMapper.updateGoodsPrice(String.valueOf(minPrice),String.valueOf(minPrice),id);

        return true;
    }
}
