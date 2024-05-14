package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
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

import java.util.*;

@RestController
@RequestMapping("/seller")
@Validated
public class SellerController {
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

    private Integer getSellerId(){
        return UserUtil.getUserId();
    }

    @PostMapping("/login")
    @ResponseBody
    public Result<Map<String,Object>> login(@Validated @RequestBody UserLoginRequest userLoginRequest){
        Seller loginSeller=sellerService.findByName(userLoginRequest.getAccount());
        if(loginSeller==null){
            return Result.error("用户不存在！");
        }
        else{
            if(Md5Util.checkPassword(userLoginRequest.getPassword(),loginSeller.getPassword())){
                // 添加日志
                String ip = IpUtil.getIpAddr(servletRequest);
                logService.logLogin("seller",loginSeller.getId(),ip,"login");
                return Result.success(UserUtil.login(loginSeller,"seller",stringRedisTemplate));
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
        logService.logLogin("seller",UserUtil.getUserId(),ip,"logout");
        UserUtil.logout(servletRequest.getHeader("Authorization"),stringRedisTemplate);
        return Result.success();
    }

    @GetMapping("/category")
    @ResponseBody
    public Result<List<Category>> getSellCategory(){
        return Result.success(sellerService.getSellCategory(getSellerId()));
    }

    // 获取负责商品
    @GetMapping("/goods")
    @ResponseBody
    public Result<List<Map<String,Object>>> getGoods(@RequestParam("id") Integer category_id,
            @Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
            @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize,
                                                     @RequestParam("type") String type){
        Integer seller_id= getSellerId();
        // 获取负责的商品id列表
        List<Integer> goodsIds=sellerService.getSellGoodsId(seller_id,category_id, type);
        // 获取商品属性
        List<Map<String,Object>> goodsList=new ArrayList<>();
        int start=(page-1)*pageSize;
        if(start<goodsIds.size()){
            for(;start<Integer.min(page*pageSize,goodsIds.size());start++){
                Integer goods_id=goodsIds.get(start);
                Goods goods=goodsService.getGoodsById(goods_id);
                // 计算总库存、总销售
                int totalStock=0;
                int totalSales=0;
                int totalVolume=0;
                List<Sku> skus = goods.getSkus();
                for(Sku sku:skus){
                    int stock=sku.getInventory();
                    totalStock+=stock;
                    int sales=sku.getSalesCount();
                    totalSales+=sales;
                    int volume=sku.getSalesVolume();
                    totalVolume+=volume;
                }
                // 拼接结果
                Map<String,Object> result=new HashMap<>();
                result.put("id",goods.getId());
                result.put("name",goods.getName());
                result.put("price",goods.getPrice());
                result.put("picture",goods.getPicture());
                result.put("totalStock",totalStock);
                result.put("totalSales",totalSales);
                result.put("totalVolume",totalVolume);
                result.put("specs",goods.getSpecs());
                result.put("skus",goods.getSkus());
                goodsList.add(result);
            }
            String value="goods category_id:"+category_id.toString()+" page:"+page.toString()+" pageSize:"+pageSize.toString();
            logService.logSeller(seller_id,IpUtil.getIpAddr(servletRequest),"get",value);
            return Result.success(goodsList);
        }
        else{
            return Result.error("参数超出范围！");
        }
    }

    // 获取负责商品总数
    @GetMapping("/goods/count")
    @ResponseBody
    public Result<Integer> getGoodsCount(@RequestParam("id") Integer category_id,
                                         @RequestParam("type") String type){
        Integer seller_id= getSellerId();
        // 获取负责的商品id列表
        List<Integer> goodsIds=sellerService.getSellGoodsId(seller_id,category_id, type);
        return Result.success(goodsIds.size());
    }

    // 添加商品
    @PostMapping("/goods")
    @ResponseBody
    public Result<Integer> addGoods(@Validated @RequestBody SellerGoodsRequest request){
        Integer seller_id= getSellerId();
        // 检查specs、sku里的specs数目
        int specs_size=request.getSpecs().size();
        if(specs_size>2){ return Result.error("规格数目超出限制"); }
        for(Sku sku: request.getSkus()){
            if(sku.getSpecs().size()!=specs_size){ return Result.error("规格数目不符"); }
        }
        Integer goods_id=sellerService.addGoods(seller_id,request);
        String value="goods id:"+goods_id.toString()+" name:"+request.getName();
        logService.logSeller(seller_id,IpUtil.getIpAddr(servletRequest),"post add",value);
        return Result.success(goods_id);
    }

    // 删除商品（伪）
    @DeleteMapping("/goods")
    @ResponseBody
    public Result deleteGoodsFake(@RequestParam("id") Integer id){
        Integer seller_id= getSellerId();
        sellerService.deleteGoodsFake(id,seller_id);
        String value="goods id:"+id.toString();
        logService.logSeller(seller_id,IpUtil.getIpAddr(servletRequest),"delete fake",value);
        return Result.success();
    }

    // 修改商品价格、库存
    @PostMapping("/modify/goods/sku")
    @ResponseBody
    public Result updateGoodsPriceAndInventory(@RequestBody SellerSkuRequest request){
        Integer seller_id= getSellerId();
        StringBuilder value=new StringBuilder("sku goods_id:"+request.getGoods_id().toString());
        for(Sku sku:request.getSkus()){
            value.append(" skuId:").append(sku.getId()).append("|price:").append(sku.getPrice()).append("|inventory:").append(sku.getInventory().toString());
            sellerService.updateGoodsPriceAndInventory(sku.getId(),sku.getPrice(),sku.getInventory(),request.getGoods_id(),seller_id);
        }
        logService.logSeller(seller_id,IpUtil.getIpAddr(servletRequest),"update",value.toString());
        return Result.success();
    }

    // 获取所负责商品相关订单项目
    @GetMapping("/order")
    @ResponseBody
    public Result getOrder(@Validated @RequestParam("orderState") @Range(min=0,max=6,message = "参数错误") Integer orderState,
                           @Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
                           @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize){
        Integer seller_id = getSellerId();
//        先校验参数是否有效
        Integer total=sellerService.getOrderItemCounts(seller_id,orderState);
        Integer start=(page-1)*pageSize;
        if(start<total){
            String value="order orderState:"+orderState.toString()+" page:"+page.toString()+" pageSize:"+pageSize.toString();
            logService.logSeller(seller_id,IpUtil.getIpAddr(servletRequest),"get",value);
            return Result.success(sellerService.getOrderLists(seller_id,orderState,start,pageSize));
        }
        else{
            return Result.error("参数超出范围");
        }
    }

    // 获取用户日志（浏览/购买商品）
    @GetMapping("/log/user")
    @ResponseBody
    public Result<LogTpAndBuyResponse> getLogTpAndBuy(@Validated @RequestParam("goods_id") @Range(min=1000000,message = "参数错误") Integer goods_id,
                           @Validated @RequestParam("page") @Range(min=1,message = "参数错误") Integer page,
                           @Validated @RequestParam("pageSize") @Range(min=1,message = "参数错误") Integer pageSize){
        Integer total=logService.getLogTpAndBuyCount(goods_id);
        LogTpAndBuyResponse response=new LogTpAndBuyResponse();
        response.setTotal(total);

        Integer start=(page-1)*pageSize;
        if(start<total){
            List<Log> logs=logService.getLogTpAndBuy(goods_id,start,pageSize);
            response.setLogs(logs);
            logService.logSeller(UserUtil.getUserId(),IpUtil.getIpAddr(servletRequest),"get","log tp&buy goods_id:"+goods_id+" page:"+page+" pageSize:"+pageSize);
            return Result.success(response);
        }
        else{
            return Result.error("参数超出范围");
        }
    }
}
