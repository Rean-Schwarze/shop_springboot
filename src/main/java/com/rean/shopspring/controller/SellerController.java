package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.GoodsService;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.JwtUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

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

    private Integer getSellerId(){
        Map<String,Object> sellerMap=ThreadLocalUtil.get();
        return (Integer) sellerMap.get("id");
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
        return Result.success(sellerService.getSellCategory(getSellerId()));
    }

    // 获取负责商品
    @GetMapping("/goods")
    @ResponseBody
    public Result<List<Map<String,Object>>> getGoods(@RequestBody Map<String,Object> request){
        Integer seller_id= getSellerId();
        // 获取请求中的分类id
        Integer category_id= (Integer) request.get("id");
        // 获取负责的商品id列表
        List<Integer> goodsIds=sellerService.getSellGoodsId(seller_id,category_id);
        // 获取商品属性
        List<Map<String,Object>> goodsList=new ArrayList<>();
        for(Integer goods_id:goodsIds){
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
                totalSales+=(sales+sku.getOldSalesCount());
                int volume=sku.getSalesVolume();
                totalVolume+=(volume+sales*Integer.parseInt(sku.getPrice()));
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
        return Result.success(goodsList);
    }

    // 添加商品
    @PostMapping("/goods")
    @ResponseBody
    public Result addGoods(@Validated @RequestBody SellerGoodsRequest request){
        Integer seller_id= getSellerId();
        // 检查specs、sku里的specs数目
        int specs_size=request.getSpecs().size();
        if(specs_size>2){ return Result.error("规格数目超出限制"); }
        for(Sku sku: request.getSkus()){
            if(sku.getSpecs().size()!=specs_size){ return Result.error("规格数目不符"); }
        }
        sellerService.addGoods(seller_id,request);
        return Result.success();
    }

    // 删除商品（伪）
    @DeleteMapping("/goods")
    @ResponseBody
    public Result deleteGoodsFake(@RequestParam("id") Integer id){
        Integer seller_id= getSellerId();
        sellerService.deleteGoodsFake(id,seller_id);
        return Result.success();
    }

    // 修改商品价格、库存
    @PostMapping("/modify/goods/sku")
    @ResponseBody
    public Result updateGoodsPriceAndInventory(@RequestBody SellerSkuRequest request){
        Integer seller_id= getSellerId();
        for(Sku sku:request.getSkus()){
            sellerService.updateGoodsPriceAndInventory(sku.getId(),sku.getPrice(),sku.getInventory(),request.getGoods_id(),seller_id);
        }
        return Result.success();
    }
}
