package com.rean.shopspring.controller;

import cn.keking.anti_reptile.annotation.AntiReptile;
import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.HomeCategoryGoodsResponse;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.GoodsService;
import com.rean.shopspring.service.HomeService;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @RequestMapping("/category/head")
    @ResponseBody
    public Result<List<Map<String,Object>>> getAllCategories(){
        List<Map<String,Object>> categoryList=homeService.getAllCategories();
        return Result.success(categoryList);
    }

    @AntiReptile
    @RequestMapping("/new")
    @ResponseBody
    public Result<List<Goods>> getNewGoods(@RequestParam("limit") Integer limit){
        List<Goods> goodsList=homeService.getNewGoods(limit);
        return Result.success(goodsList);
    }

    @AntiReptile
    @RequestMapping("/banner")
    @ResponseBody
    public Result<List<Banner>> getBanners(){
        List<Banner> bannerList=homeService.getBanners();
        return Result.success(bannerList);
    }

    @RequestMapping("/category")
    @ResponseBody
    public Result<Map<String,Object>> getSubCategories(@RequestParam("id") String id){
        Map<String,Object> category=homeService.getSubCategoriesById(Integer.parseInt(id));
        return Result.success(category);
    }

    @AntiReptile
    @RequestMapping("/goods")
    @ResponseBody
    public Result<List<HomeCategoryGoodsResponse>> getGoodsAll(@Validated @RequestParam("limit") @Range(min=1,message = "参数错误") Integer limit){
        return Result.success(homeService.getGoodsAll(limit));
    }
}
