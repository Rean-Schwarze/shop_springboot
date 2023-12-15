package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private HomeService homeService;

    @RequestMapping("/category/head")
    @ResponseBody
    public Result<List<Category>> getAllCategories(){
        List<Category> categoryList=homeService.getAllCategories();
        return Result.success(categoryList);
    }

    @RequestMapping("/new")
    @ResponseBody
    public Result<List<Goods>> getNewGoods(@RequestParam("limit") Integer limit){
        List<Goods> goodsList=homeService.getNewGoods(limit);
        return Result.success(goodsList);
    }

    @RequestMapping("/banner")
    @ResponseBody
    public Result<List<Banner>> getBanners(){
        List<Banner> bannerList=homeService.getBanners();
        return Result.success(bannerList);
    }
}
