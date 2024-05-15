package com.rean.shopspring.controller;

import cn.keking.anti_reptile.annotation.AntiReptile;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @AntiReptile
    @RequestMapping("/goods")
    @ResponseBody
    public Result<Goods> getGoodsById(@RequestParam("id") String id){
        Goods goods=goodsService.getGoodsById(Integer.parseInt(id));
        return Result.success(goods);
    }

    @AntiReptile
    @RequestMapping("/goods/relevant")
    @ResponseBody
    public Result<List<Map<String,String>>> getRelevantGoods(@RequestParam("limit") int limit){
        List<Map<String,String>> goodsList=goodsService.getGoodsByRelevant(limit);
        return Result.success(goodsList);
    }
}
