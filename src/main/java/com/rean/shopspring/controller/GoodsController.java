package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @RequestMapping("/goods")
    @ResponseBody
    public Result<Goods> getGoodsById(@RequestParam("id") String id){
        Goods goods=goodsService.getGoodsById(Integer.parseInt(id));
        return Result.success(goods);
    }
}
