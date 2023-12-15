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

    @Override
    public Category getSubCategoriesById(Integer id){
        Category category=homeMapper.getCategoryById(id);
        List<Category> subCategoryList=homeMapper.getSubCategoriesByParentId(id);
        for(Category sub:subCategoryList){
            List<Goods> goodsList_1=homeMapper.getGoodsBySubCategoryId(Integer.parseInt(sub.getId()));
            List<Goods> goodsList_2=homeMapper.getGoodsBySubCategoryId2(Integer.parseInt(sub.getId()));
            if(goodsList_1==null){
                for (Goods good:goodsList_2){
                    String picture=homeMapper.getGoodsPicturesById(Integer.parseInt(good.getId()));
                    good.setPicture(picture);
                }
                sub.setGoods(goodsList_2);
            }
            else{
                for (Goods good:goodsList_1){
                    String picture=homeMapper.getGoodsPicturesById(Integer.parseInt(good.getId()));
                    good.setPicture(picture);
                }
                sub.setGoods(goodsList_1);
            }
        }
        category.setChildren(subCategoryList);
        return category;
    }
}
