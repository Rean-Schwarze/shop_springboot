package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.CartService;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
//    获取预处理订单（计算总价）
    public Result<Map<String,Object>> generateOrderPre(@RequestBody Map<String,List<String>> body){
        int user_id=userService.getUserIdIfLogin();
        List<Address> userAddresses=userService.getAddress(user_id);
        List<String> skus=body.get("skus");
        List<CartItem> goods=cartService.getCartList(skus);
        Integer goodsCount=0;
        int totalPrice=0;
        int postFee=0;
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
    @RequestMapping(value = "/member/order",method = RequestMethod.POST)
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

    @CrossOrigin
    @GetMapping("/member/order")
    @ResponseBody
    public Result<Map<String,Object>> getUserOrder(@RequestParam("orderState") int orderState,
                                                         @RequestParam("page") int page,
                                                         @RequestParam("pageSize") int pageSize){
        int user_id=userService.getUserIdIfLogin();
        int counts=0;
        if(orderState==0){
            counts=orderService.getUserOrderCounts(user_id);
        }
        else{
            counts=orderService.getUserOrderCountsByState(user_id,orderState);
        }
        int start=(page-1)*pageSize;
        List<Map<String,Object>> completeOrderList=new ArrayList<>();
        if(start<counts){
            completeOrderList=orderService.getUserOrder(user_id,start,pageSize,orderState);
        }
        Map<String,Object> resultOrder=new HashMap<>();
        resultOrder.put("counts",counts);
        resultOrder.put("page",page);
        resultOrder.put("pageSize",pageSize);
        resultOrder.put("items",completeOrderList);

        return Result.success(resultOrder);
    }
}
