package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.HomeMapper;
import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.HomeCategoryGoodsResponse;
import com.rean.shopspring.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper homeMapper;

    @Override
    public List<Map<String,Object>> getAllCategories(){
        List<Map<String,Object>> result=new ArrayList<>();
        List<Category> categoryList=homeMapper.getAllCategories();
        for (Category category:categoryList){
            List<Category> subCategoryList=homeMapper.getSubCategoriesByParentId(category.getId());
            List<Goods> goodsList=homeMapper.getGoodsByCategoryAndLimit(category.getId(),8);
            for (Goods good:goodsList){
                String picture=homeMapper.getGoodsPicturesById(good.getId());
                good.setPicture(picture);
            }

            Map<String,Object> tmp=new HashMap<>();
            tmp.put("id",category.getId());
            tmp.put("name",category.getName());
            tmp.put("picture",category.getPicture());
            tmp.put("children",subCategoryList);
            tmp.put("goods",goodsList);
            result.add(tmp);
        }
        return result;
    }

    @Override
    public List<Goods> getNewGoods(Integer limit){
        List<Goods> goodsList=homeMapper.getNewGoods(limit);
        for (Goods good:goodsList){
            String picture=homeMapper.getGoodsPicturesById(good.getId());
            good.setPicture(picture);
        }
        return goodsList;
    }

    @Override
    public List<Banner> getBanners(){
        return homeMapper.getBanners();
    }

    @Override
    public Map<String,Object> getSubCategoriesById(Integer id){
        Map<String,Object> result=new HashMap<>();
        Category category=homeMapper.getCategoryById(id);
        List<Map<String,Object>> result_sub=new ArrayList<>();
        List<Category> subCategoryList=homeMapper.getSubCategoriesByParentId(id);
        for(Category sub:subCategoryList){
            Map<String,Object> tmp=new HashMap<>();
            tmp.put("id",sub.getId());
            tmp.put("name",sub.getName());
            tmp.put("picture",sub.getPicture());
            List<Goods> goodsList_1=homeMapper.getGoodsBySubCategoryId(sub.getId());
            List<Goods> goodsList_2=homeMapper.getGoodsBySubCategoryId2(sub.getId());
            if(goodsList_1==null){
                for (Goods good:goodsList_2){
                    String picture=homeMapper.getGoodsPicturesById(good.getId());
                    good.setPicture(picture);
                }
                tmp.put("goods",goodsList_2);
            }
            else{
                for (Goods good:goodsList_1){
                    String picture=homeMapper.getGoodsPicturesById(good.getId());
                    good.setPicture(picture);
                }
                tmp.put("goods",goodsList_1);
            }
            result_sub.add(tmp);
        }
        result.put("id",category.getId());
        result.put("name",category.getName());
        result.put("picture",category.getPicture());
        result.put("children",result_sub);
        return result;
    }

    @Override
    public List<HomeCategoryGoodsResponse> getGoodsAll(Integer limit){
        List<HomeCategoryGoodsResponse> categoryList=homeMapper.getAllCategories2();
        for (HomeCategoryGoodsResponse category:categoryList){
            List<Goods> goodsList=homeMapper.getGoodsByCategoryAndLimit(category.getId(), limit);
            List<HomeCategoryGoodsResponse.GoodsLite> list=new ArrayList<>();
            for (Goods good:goodsList){
                String picture=homeMapper.getGoodsPicturesById(good.getId());
                HomeCategoryGoodsResponse.GoodsLite goods=new HomeCategoryGoodsResponse.GoodsLite(good.getId(),good.getName(),good.getDesc(),picture,good.getPrice());
                list.add(goods);
            }
            category.setGoods(list);
        }
        return categoryList;
    }
}
