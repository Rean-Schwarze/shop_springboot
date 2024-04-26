package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.UserLoginRequest;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/seller")
@Validated
public class SellerController {
    @Autowired
    private SellerService sellerService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/login")
    @ResponseBody
    public Result<Map<String,Object>> login(@Validated @RequestBody UserLoginRequest userLoginRequest){
        Seller loginSeller=sellerService.findByName(userLoginRequest.getAccount());
        if(loginSeller==null){
            return Result.error("用户不存在！");
        }
        else{
            if(Md5Util.checkPassword(userLoginRequest.getPassword(),loginSeller.getPassword())){
                Map<String,Object> sellerMap=new HashMap<>();
                sellerMap.put("type","seller");
                sellerMap.put("id",loginSeller.getId());
                sellerMap.put("name",loginSeller.getName());
                sellerMap.put("avatar",loginSeller.getAvatar());

                String token = JwtUtil.genToken(sellerMap);
                sellerMap.put("token",token);
                //把token存储到redis中
                ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
                operations.set(token,"sellerId: "+loginSeller.getId().toString(),12, TimeUnit.HOURS);

                return Result.success(sellerMap);
            }
            else{
                return Result.error("密码错误！");
            }
        }
    }

    @GetMapping("/category")
    @ResponseBody
    public Result<List<Category>> getSellCategory(){
        Map<String,Object> sellerMap=ThreadLocalUtil.get();
        if(!Objects.equals(sellerMap.get("type").toString(), "seller")){
            return Result.error("请求错误！");
        }
        else{
            return Result.success(sellerService.getSellCategory((Integer) sellerMap.get("id")));
        }
    }
}
