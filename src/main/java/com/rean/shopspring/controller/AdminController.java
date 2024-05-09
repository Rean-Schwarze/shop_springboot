package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Admin;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.pojo.UserLoginRequest;
import com.rean.shopspring.service.AdminService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import com.rean.shopspring.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

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
}
