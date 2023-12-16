package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @RequestMapping("/member/cart")
    @ResponseBody
    public Result<CartItem> addCart(@RequestBody Map<String,String> orderItem){
        return Result.success(cartService.addCart(Integer.parseInt(orderItem.get("id")),orderItem.get("skuId"),
                Integer.parseInt(orderItem.get("count"))));
    }

    @RequestMapping("/member/cartlist")
    @ResponseBody
    public Result<List<CartItem>> getCartList(){
        return Result.success(cartService.getCartList());
    }

    @RequestMapping("/member/cart/merge")
    @ResponseBody
    public Result mergeCart(@RequestBody List<Map<String,Object>> cartList){
        cartService.mergeCartList(cartList);
        return Result.success();
    }

    @RequestMapping("/member/cart/delete")
    @ResponseBody
    public Result deleteCart(@RequestBody Map<String,Object> list){
        List<String> skuIds= (List<String>) list.get("ids");
        boolean flag= cartService.deleteCartList(skuIds);
        if(flag){
            return Result.success();
        }
        else {
            return Result.error("删除失败！");
        }
    }
}
