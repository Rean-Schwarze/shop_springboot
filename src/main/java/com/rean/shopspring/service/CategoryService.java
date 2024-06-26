package com.rean.shopspring.service;

import java.util.Map;

public interface CategoryService {
    Map<String,Object> getCategoryFilter(Integer id);

    Map<String,Object> getSubCategoryAndGoods(String categoryId, String page, String pageSize, String sortField);
}
