package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.HomeMapper;
import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    private HomeMapper homeMapper;

    @Override
    public List<Category> getAllCategories(){
        List<Category> categoryList=homeMapper.getAllCategories();
        for (Category category:categoryList){
            List<Category> subCategoryList=homeMapper.getSubCategoriesByParentId(Integer.parseInt(category.getId()));
            category.setChildren(subCategoryList);
            List<Goods> goodsList=homeMapper.get8GoodsByCategory(Integer.parseInt(category.getId()));
            for (Goods good:goodsList){
                String picture=homeMapper.getGoodsPicturesById(Integer.parseInt(good.getId()));
                good.setPicture(picture);
            }
            category.setGoods(goodsList);

        }
        return categoryList;
    }

    @Override
    public List<Goods> getNewGoods(Integer limit){
        List<Goods> goodsList=homeMapper.getNewGoods(limit);
        for (Goods good:goodsList){
            String picture=homeMapper.getGoodsPicturesById(Integer.parseInt(good.getId()));
            good.setPicture(picture);
        }
        return goodsList;
    }

    @Override
    public List<Banner> getBanners(){
        return homeMapper.getBanners();
    }
}
