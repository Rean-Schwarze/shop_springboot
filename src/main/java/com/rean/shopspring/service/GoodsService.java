package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Goods;

import java.util.List;
import java.util.Map;

public interface GoodsService {
    Goods getGoodsById(Integer id);
    List<Map<String,String>> getGoodsByRelevant(Integer limit);
}
