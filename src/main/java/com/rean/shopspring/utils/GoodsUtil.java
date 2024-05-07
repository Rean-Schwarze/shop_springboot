package com.rean.shopspring.utils;

import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.Spec_values;
import org.jetbrains.annotations.NotNull;

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
}
