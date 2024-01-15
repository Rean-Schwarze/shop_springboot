package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public Goods getGoodsById(Integer id){
        Goods goods=goodsMapper.getGoodsById(id);
        goods.setBrand(goodsMapper.getBrandById(goods.getBrand_id()));
        goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(Integer.parseInt(goods.getId())));

        List<Specs> specs=goodsMapper.getSpcesByGoodsId(Integer.parseInt(goods.getId()));
        for(Specs spec:specs){
            List<Spec_values> spec_values=goodsMapper.getSpcesValueBySpecsId(spec.getId());
            spec.setValues(spec_values);
        }
        goods.setSpecs(specs);

        List<Sku> skus=goodsMapper.getSkuByGoodsId(goods.getId());
        for(Sku sku:skus){
            List<Spec_values> spec_values=goodsMapper.getSkuSpecBySkuId(sku.getId());
            List<Spec_sku> sk=new LinkedList<Spec_sku>();
            for(Spec_values sv:spec_values){
                Spec_sku ss=new Spec_sku(goodsMapper.getSpecNameBySpceValueId(Integer.parseInt(sv.getId())),sv.getName());
                sk.add(ss);
            }
            sku.setSpecs(sk);
        }
        goods.setSkus(skus);

        goods.setDetails(new Detail(goodsMapper.getDetailPicturesByGoodsId(Integer.parseInt(goods.getId())),
                goodsMapper.getDetailPropertiesByGoodsId(Integer.parseInt(goods.getId()))));

        Category sub=categoryMapper.getSubCategoryById(goods.getSub_category_id());
        Category par=categoryMapper.getCategoryById(goods.getCategory_id());
        List<Category> categories=new LinkedList<>();
        categories.add(sub);
        categories.add(par);
        goods.setCategories(categories);

        return goods;
    }

    @Override
    public List<Map<String,String>> getGoodsByRelevant(Integer limit){
        int total=goodsMapper.getGoodsCount();
        if(limit+1>total){
            limit=total-1;
        }
        List<Goods> goodsList=goodsMapper.getGoodsByRandom(limit+1);
        List<Map<String,String>> goodsListRelevant=new LinkedList<>();
        for(int i=0;i<goodsList.size();i++){
            if (Objects.equals(goodsList.get(i).getId(), "1000002")){
                continue;
            }
            goodsList.get(i).setMainPictures(goodsMapper.getMainPicturesByGoodsId(Integer.parseInt(goodsList.get(i).getId())));
            Map<String,String> goodsMap=new HashMap<>();
            goodsMap.put("id",goodsList.get(i).getId());
            goodsMap.put("name",goodsList.get(i).getName());
            goodsMap.put("desc",goodsList.get(i).getDesc());
            goodsMap.put("price",goodsList.get(i).getPrice());
            goodsMap.put("picture",goodsList.get(i).getMainPictures().get(0));
            if(goodsListRelevant.size()<4){
                goodsListRelevant.add(goodsMap);
            }
        }
        return goodsListRelevant;
    }

}
