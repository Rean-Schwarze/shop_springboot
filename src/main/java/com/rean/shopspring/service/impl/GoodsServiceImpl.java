package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.GoodsService;
import com.rean.shopspring.utils.GoodsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryMapper categoryMapper;

//    获取商品所有属性
    @Override
    public Goods getGoodsById(Integer id){
        Goods goods=goodsMapper.getGoodsById(id);
        goods.setBrand(goodsMapper.getBrandById(goods.getBrandId()));
        goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(goods.getId()));
        goods.setPicture(goods.getMainPictures().get(0));

//        获取specs
        List<Specs> specs=goodsMapper.getSpcesByGoodsId(goods.getId());
        for(Specs spec:specs){
            List<Spec_values> spec_values=goodsMapper.getSpcesValueBySpecsId(spec.getId());
            spec.setValues(spec_values);
        }
        goods.setSpecs(specs);

//        获取skus
        List<Sku> skus=goodsMapper.getSkuByGoodsId(goods.getId());
        for(Sku sku:skus){
            List<Spec_values> spec_values=new ArrayList<>();
            Spec_values tmp=goodsMapper.getSkuSpecBySkuId(sku.getId());
            if(tmp!=null){
                spec_values.add(tmp);
            }
            tmp=goodsMapper.getSkuSpecBySkuId_2(sku.getId());
            if(tmp!=null){
                spec_values.add(tmp);
            }
            List<Spec_sku> sk=new LinkedList<Spec_sku>();
            for(Spec_values sv:spec_values){
                Spec_sku ss=new Spec_sku(goodsMapper.getSpecNameBySpceValueId(sv.getId()),sv.getName());
                sk.add(ss);
            }
            sku.setSpecs(sk);
            sku.setAttrsText(GoodsUtil.getAttrsText(sku.getId(),goodsMapper));
        }
        goods.setSkus(skus);

        goods.setDetails(new Detail(goodsMapper.getDetailPicturesByGoodsId(goods.getId()),
                goodsMapper.getDetailPropertiesByGoodsId(goods.getId())));

//        设置所属分类
        Category sub=categoryMapper.getSubCategoryById(goods.getSubCategoryId());
        Category par=categoryMapper.getCategoryById(goods.getCategoryId());
        List<Category> categories=new LinkedList<>();
        categories.add(sub);
        categories.add(par);
        goods.setCategories(categories);

        return goods;
    }

//    获取“猜你喜欢”商品的属性（id、name、desc、price、picture）
    @Override
    public List<Map<String,String>> getGoodsByRelevant(Integer limit){
        int total=goodsMapper.getGoodsCount();
        if(limit+1>total){
            limit=total-1;
        }
        List<Goods> goodsList=goodsMapper.getGoodsByRandom(limit+1);

        List<Map<String,String>> goodsListRelevant=new LinkedList<>();
        for (Goods goods : goodsList) {
            if (Objects.equals(goods.getId(), "1000002")) {
                continue;
            }
            goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(goods.getId()));
            Map<String, String> goodsMap = new HashMap<>();
            goodsMap.put("id", String.valueOf(goods.getId()));
            goodsMap.put("name", goods.getName());
            goodsMap.put("desc", goods.getDesc());
            goodsMap.put("price", goods.getPrice());
            goodsMap.put("picture", goods.getMainPictures().get(0));
            if (goodsListRelevant.size() < 4) {
                goodsListRelevant.add(goodsMap);
            }
        }
        return goodsListRelevant;
    }

    @Override
    public List<Integer> getGoodsIds(Integer category_id, String type){
        if(Objects.equals(type, "all")) return goodsMapper.getGoodsIdsAll();
        else if(Objects.equals(type,"main")) return goodsMapper.getGoodsIdsByCategory(category_id);
        else return goodsMapper.getGoodsIdsBySubCategory(category_id);
    }
}
