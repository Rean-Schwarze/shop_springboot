package com.rean.shopspring.service;


import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Goods;

import java.util.List;

public interface HomeService {
    List<Category> getAllCategories();

    List<Goods> getNewGoods(Integer limit);

    List<Banner> getBanners();

    Category getSubCategoriesById(Integer id);
}
