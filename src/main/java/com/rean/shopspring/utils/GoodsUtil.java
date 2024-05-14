package com.rean.shopspring.utils;

import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.AdSeGoodsResponse;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Sku;
import com.rean.shopspring.pojo.Spec_values;
import com.rean.shopspring.service.GoodsService;
import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

public class GoodsUtil {
    @NotNull
    public static String getAttrsText(Integer skuId, GoodsMapper goodsMapper) {
        List<Spec_values> spec_values=new ArrayList<>();
        Spec_values tmp= goodsMapper.getSkuSpecBySkuId(skuId);
        if(tmp!=null){
            spec_values.add(tmp);
        }
        tmp= goodsMapper.getSkuSpecBySkuId_2(skuId);
        if(tmp!=null){
            spec_values.add(tmp);
        }
        List<String> attrsTexts=new ArrayList<>();
        for(Spec_values values:spec_values){
            if(values!=null){
                String spec_name= goodsMapper.getSpecNameBySpceValueId(values.getId());
                attrsTexts.add(spec_name+"："+values.getName());
            }
        }
        return String.join("\t",attrsTexts);
    }

    public static String getGoodsForEach(@RequestParam("id") Integer category_id, @RequestParam("page") @Validated @Range(min = 1, message = "参数错误") Integer page, @RequestParam("pageSize") @Validated @Range(min = 1, message = "参数错误") Integer pageSize, List<Integer> goodsIds, List<AdSeGoodsResponse> goodsList, int start, GoodsService goodsService) {
        for(;start<Integer.min(page*pageSize,goodsIds.size());start++){
            Integer goods_id=goodsIds.get(start);
            Goods goods= goodsService.getGoodsById(goods_id);
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
            AdSeGoodsResponse result=new AdSeGoodsResponse(goods.getId(),goods.getName(),goods.getPrice(),goods.getPicture(),totalStock,totalSales,totalVolume,goods.getSpecs(),goods.getSkus());
            goodsList.add(result);
        }
        return "goods category_id:"+category_id.toString()+" page:"+page.toString()+" pageSize:"+pageSize.toString();
    }
}
