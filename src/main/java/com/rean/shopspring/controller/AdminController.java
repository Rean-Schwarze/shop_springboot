package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.AdminService;
import com.rean.shopspring.service.LogService;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.*;
import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private LogService logService;
    @Autowired
    private HttpServletRequest servletRequest;

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
                // 添加日志
                String ip = IpUtil.getIpAddr(servletRequest);
                logService.logLogin("admin",loginAdmin.getId(),ip,"login");
                return Result.success(UserUtil.login(loginAdmin,"admin",stringRedisTemplate));
            }
            else{
                return Result.error("密码错误！");
            }
        }
    }

    @PostMapping("/logout")
    @ResponseBody
    public Result logout(){
        // 添加日志
        String ip = IpUtil.getIpAddr(servletRequest);
        logService.logLogin("admin",UserUtil.getUserId(),ip,"logout");
        UserUtil.logout(servletRequest.getHeader("Authorization"),stringRedisTemplate);
        return Result.success();
    }

    @GetMapping("/seller")
    @ResponseBody
    public Result<List<AdminSellerResponse>> getSellerLists(@Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
                                 @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize,
                                 @Validated @RequestParam("brand_id") @Range(min=0,message = "参数错误") Integer brand_id){
        Integer count=adminService.getSellerCount(brand_id,true);
        int start=(page-1)*pageSize;
        if(start<count){
            List<AdminSellerResponse> responses=adminService.getSellerLists(brand_id,start,pageSize,true);
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

    @PostMapping("/seller/reset/password")
    @ResponseBody
    public Result sellerPasswordReset(@Validated @RequestBody PasswordResetRequest request){
        Seller seller=sellerService.findById(request.getId());
        if(seller!=null){
            sellerService.updatePassword(request.getId(), request.getNewPassword());
            return Result.success();
        }
        else{
            return Result.error("用户不存在！");
        }
    }

    @DeleteMapping("/seller")
    @ResponseBody
    public Result deleteSeller(@Validated @RequestParam("id") @Range(min=1,message = "参数错误") Integer id){
        Seller seller=sellerService.findById(id);
        if(seller!=null){
            adminService.deleteSellerFake(id);
            return Result.success();
        }
        else {
            return Result.error("用户不存在！");
        }
    }
}
