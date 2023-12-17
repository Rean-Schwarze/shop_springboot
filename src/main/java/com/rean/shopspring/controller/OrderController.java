package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.CartService;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @CrossOrigin
    @RequestMapping("/member/order/pre")
    @ResponseBody
    public Result<Map<String,Object>> genetateOrderPre(){
        Map<String, Object> u= ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));
        List<Address> userAddresses=userService.getAddress(user_id);
        List<CartItem> goods=cartService.getCartList();
        Integer goodsCount=0;
        int totalPrice=0;
        int postFee=8;
        for(CartItem good:goods){
            goodsCount+=good.getCount();
            int total_price=good.getCount()*Integer.parseInt(good.getNowPrice().split("\\.")[0]);
            totalPrice+=total_price;
            good.setPayPrice(good.getPrice());
            good.setTotalPrice(String.valueOf(total_price));
            good.setTotalPayPrice(String.valueOf(total_price+postFee));
        }
        int totalPayPrice=totalPrice+postFee;
        OrderPre summary=new OrderPre(0,totalPrice,goodsCount,true,0,totalPayPrice,postFee);

        Map<String,Object> result=new HashMap<>();
        result.put("userAddresses",userAddresses);
        result.put("goods",goods);
        result.put("summary",summary);

        return Result.success(result);
    }

    @CrossOrigin
    @RequestMapping("/member/order")
    @ResponseBody
    public Result<Order> postOrder(@RequestBody Map<String,Object> orderOption){
        return Result.success(orderService.postOrder(orderOption));
    }

    @CrossOrigin
    @RequestMapping("/member/order/{id}")
    @ResponseBody
    public Result<Order> getOrder(@PathVariable String id){
        return Result.success(orderService.getOrder(Integer.parseInt(id)));
    }
}
