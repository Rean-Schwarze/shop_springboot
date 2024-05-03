package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @CrossOrigin
    @RequestMapping("/member/cart")
    @ResponseBody
    public Result<CartItem> addCart(@RequestBody Map<String,String> orderItem){
        return Result.success(cartService.addCart(Integer.parseInt(orderItem.get("id")), Integer.valueOf(orderItem.get("skuId")),
                Integer.parseInt(orderItem.get("count"))));
    }

    @CrossOrigin
    @RequestMapping("/member/cartlist")
    @ResponseBody
    public Result<List<CartItem>> getCartList(){
        return Result.success(cartService.getCartList());
    }

    @CrossOrigin
    @RequestMapping("/member/cart/merge")
    @ResponseBody
    public Result mergeCart(@RequestBody List<Map<String,Object>> cartList){
        cartService.mergeCartList(cartList);
        return Result.success();
    }

    @CrossOrigin
    @RequestMapping("/member/cart/delete")
    @ResponseBody
    public Result deleteCart(@RequestBody Map<String,Object> list){
        List<Integer> skuIds= (List<Integer>) list.get("ids");
        boolean flag= cartService.deleteCartList(skuIds);
        if(flag){
            return Result.success();
        }
        else {
            return Result.error("删除失败！");
        }
    }
}
