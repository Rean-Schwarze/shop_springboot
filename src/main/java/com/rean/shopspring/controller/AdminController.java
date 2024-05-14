package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.AdminService;
import com.rean.shopspring.service.GoodsService;
import com.rean.shopspring.service.LogService;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private LogService logService;
    @Autowired
    private HttpServletRequest servletRequest;

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

    @GetMapping("/brand")
    @ResponseBody
    public Result<List<Brand>> getBrand(){
        return Result.success(adminService.getBrandsAll());
    }

    @GetMapping("/seller/count")
    @ResponseBody
    public Result<Integer> getSellerCount(@Validated @RequestParam("brand_id") @Range(min=0,message = "参数错误") Integer brand_id){
        return Result.success(adminService.getSellerCount(brand_id,true));
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
            String value="seller brandId:"+brand_id.toString()+" page"+page.toString()+" pageSize:"+pageSize.toString();
            logService.logAdmin(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"get",value);
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
            Integer seller_id=adminService.sellerRegister(request);
            String value="seller id:"+seller_id.toString();
            logService.logAdmin(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"register",value);
            return Result.success(seller_id);
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
            String value="seller_password seller_id:"+request.getId().toString();
            logService.logAdmin(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"update",value);
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
            String value="seller id:"+id.toString();
            logService.logAdmin(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"delete fake",value);
            return Result.success();
        }
        else {
            return Result.error("用户不存在！");
        }
    }

    // 获取商品总数
    @GetMapping("/goods/count")
    @ResponseBody
    public Result<Integer> getGoodsCount(@RequestParam("id") Integer category_id,
                                         @RequestParam("type") String type){
        return Result.success(goodsService.getGoodsIds(category_id,type).size());
    }

    // 获取商品
    @GetMapping("/goods")
    @ResponseBody
    public Result<List<AdSeGoodsResponse>> getGoods(@RequestParam("id") Integer category_id,
                                                     @Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
                                                     @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize,
                                                     @RequestParam("type") String type){
        // 获取负责的商品id列表
        List<Integer> goodsIds=goodsService.getGoodsIds(category_id,type);
        // 获取商品属性
        List<AdSeGoodsResponse> goodsList=new ArrayList<>();
        int start=(page-1)*pageSize;
        if(start<goodsIds.size()){
            String value=GoodsUtil.getGoodsForEach(category_id, page, pageSize, goodsIds, goodsList, start, goodsService);
            logService.logAdmin(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"get",value);
            return Result.success(goodsList);
        }
        else{
            return Result.error("参数超出范围！");
        }
    }

    // 修改商品价格、库存
    @PostMapping("/modify/goods/sku")
    @ResponseBody
    public Result updateGoodsPriceAndInventory(@RequestBody SellerSkuRequest request){
        StringBuilder value=new StringBuilder("sku goods_id:"+request.getGoods_id().toString());
        for(Sku sku:request.getSkus()){
            value.append(" skuId:").append(sku.getId()).append("|price:").append(sku.getPrice()).append("|inventory:").append(sku.getInventory().toString());
            sellerService.updateGoodsPriceAndInventory(sku.getId(),sku.getPrice(),sku.getInventory(),request.getGoods_id(),UserUtil.getUserId());
        }
        logService.logSeller(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"update",value.toString());
        return Result.success();
    }
}
