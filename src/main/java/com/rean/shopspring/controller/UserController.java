package com.rean.shopspring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
//    用户注册
    @PostMapping("/register")
    public Result register(@Validated @RequestBody UserRegisterRequest user){
        String username=user.getAccount();
        String phone=user.getPhone();
        String password=user.getPassword();
        String email=user.getEmail();
        String nickname=user.getNickname();


        if(username==null || Objects.equals(username, "")){
            username="用户"+phone.substring(0,3)+"***"+phone.substring(8,11);
            nickname=username;
        }

        // 查询用户
        boolean isExists=userService.isEmailAndPhoneExists(email,phone);
        if (isExists){
            // 没有被占用
            userService.register(username,phone,password,email,nickname);
            return Result.success();
        }else{
            return Result.error("邮箱或手机号已被占用");
        }

    }


//    用户登录
    @PostMapping("/login")
    @ResponseBody
    public Result<Map<String,Object>> login(@Validated @RequestBody UserLoginRequest user) {
        String account=user.getAccount();
        String password=user.getPassword();
        // 查询用户
        User loginUser=null;
        User loginUserPhone=userService.findByPhone(account);
        User loginUserEmail=userService.findByEmail(account);
        // 判断用户是否存在
        if(loginUserPhone==null && loginUserEmail==null){
            return Result.error("邮箱或手机号错误");
        }
        else loginUser = Objects.requireNonNullElse(loginUserPhone, loginUserEmail);
        // 用户存在，检查密码是否正确（要加密一下去比对）
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> userMap=new HashMap<>();
            userMap.put("id",loginUser.getId().toString());
            userMap.put("account",loginUser.getUsername());
            userMap.put("nickname",loginUser.getNickname());
            userMap.put("email",loginUser.getEmail());
            userMap.put("avatar",loginUser.getAvatar());
            String token = JwtUtil.genToken(userMap);
            userMap.put("token",token);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,12, TimeUnit.HOURS);
            return Result.success(userMap);
        }
        return Result.error("密码错误");
    }

//    获取用户收货地址
    @GetMapping("/address")
    @ResponseBody
    public Result<List<Address>> getAddress(){
        Map<String, Object> u= ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));
        return Result.success(userService.getAddress(user_id));
    }

//    添加收货地址
    @PostMapping("/address")
    @ResponseBody
    public Result addAddress(@Validated @RequestBody Address address){
        userService.addAddress(address);
        return Result.success();
    }


    @PostMapping("/modify/basic")
    @ResponseBody
    public Result modifyBasicInfo(@Validated @RequestBody UserModifyInfoRequest userModifyInfoRequest){
        userService.modifyBasicInfo(userModifyInfoRequest);
        return Result.success();
    }

    @PostMapping("/modify/address")
    @ResponseBody
    public Result modifyAddress(@Validated @RequestBody Address address){
        userService.modifyAddress(address);
        return Result.success();
    }
}
