package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;
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
}
