package com.rean.shopspring.service;


import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.HomeCategoryGoodsResponse;

import java.util.List;
import java.util.Map;

public interface HomeService {
    List<Map<String,Object>> getAllCategories();

    List<Goods> getNewGoods(Integer limit);

    List<Banner> getBanners();

    Map<String,Object> getSubCategoriesById(Integer id);

    List<HomeCategoryGoodsResponse> getGoodsAll(Integer limit);
}
