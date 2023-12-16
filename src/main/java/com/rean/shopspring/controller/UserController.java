package com.rean.shopspring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @PostMapping("/register")
    public Result register(String username,@Pattern(regexp="^\\${4,16}$") String password, String email, String nickname, String receiver, String contact, String address){
        // 查询用户
        User u=userService.findByUserName(username);
        if (u==null){
            // 没有被占用
            userService.register(username,password,email,nickname,receiver,contact,address);
            return Result.success();
        }else{
            return Result.error("用户名已被占用");
        }

    }


    @PostMapping("/login")
    @ResponseBody
    public Result<Map<String,Object>> login(@RequestBody Map<String,String> user) {
        String username=user.get("account");
        String password=user.get("password");
        // 根据用户名查询用户
        User loginUser=userService.findByUserName(username);
        // 判断用户是否存在
        if(loginUser==null){
            return Result.error("用户名错误");
        }
        // 用户存在，检查密码是否正确（要加密一下去比对）
        if(Md5Util.getMD5String(password).equals(loginUser.getPassword())){
            Map<String,Object> userMap=new HashMap<>();
            userMap.put("id",loginUser.getId().toString());
            userMap.put("account",loginUser.getUsername());
            userMap.put("nickname",loginUser.getNickname());
            userMap.put("email",loginUser.getEmail());
            String token = JwtUtil.genToken(userMap);
            userMap.put("token",token);
            //把token存储到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token,token,1, TimeUnit.HOURS);
            return Result.success(userMap);
        }
        return Result.error("密码错误");
    }
}
