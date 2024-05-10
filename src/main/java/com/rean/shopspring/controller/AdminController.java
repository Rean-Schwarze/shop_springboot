package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.AdminService;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import com.rean.shopspring.utils.UserUtil;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private Integer getAdminId(){
        return UserUtil.getUserId();
    }

    @PostMapping("/login")
    @ResponseBody
    public Result login(@Validated @RequestBody UserLoginRequest request){
        Admin loginAdmin=adminService.findByName(request.getAccount());
        if(loginAdmin==null){
            return Result.error("用户不存在！");
        }
        else{
            if(Md5Util.checkPassword(request.getPassword(),loginAdmin.getPassword())){
                return Result.success(UserUtil.login(loginAdmin,"admin",stringRedisTemplate));
            }
            else{
                return Result.error("密码错误！");
            }
        }
    }

    @GetMapping("/seller")
    @ResponseBody
    public Result<List<AdminSellerResponse>> getSellerLists(@Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
                                 @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize,
                                 @Validated @RequestParam("brand_id") @Range(min=0,message = "参数错误") Integer brand_id){
        Integer count=adminService.getSellerCount(brand_id);
        int start=(page-1)*pageSize;
        if(start<count){
            List<AdminSellerResponse> responses=adminService.getSellerLists(brand_id,start,pageSize);
            for(AdminSellerResponse r:responses){
                List<SellerCategory> sellerCategories=sellerService.getSellCategory(r.getId());
                r.setCategory(sellerCategories);
            }
            return Result.success(responses);
        }
        else{
            return Result.error("参数超出范围");
        }
    }

    @PostMapping("/seller/register")
    @ResponseBody
    public Result<Integer> sellerRegister(@Validated @RequestBody SellerRegisterRequest request){
        Seller tmp=sellerService.findByName(request.getName());
        if(tmp==null){
            return Result.success(adminService.sellerRegister(request));
        }
        else{
            return Result.error("用户已存在！");
        }
    }
}
