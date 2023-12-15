package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

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
}
