package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsMapper goodsMapper;

    @Override
    public Map<String,Object> getCategoryFilter(Integer id){
        Map<String,Object> result=new HashMap<>();
        result.put("parentId",categoryMapper.getParentCategoryId(id));
        result.put("parentName",categoryMapper.getParentCategoryName(id));
        result.put("name",categoryMapper.getSubCategoryNameById(id));
        return result;
    }

    @Override
    public Map<String,Object> getSubCategoryAndGoods(String categoryId, String page, String pageSize, String sortField)
    {
        Map<String,Object> result=new HashMap<>();
        result.put("page",page);
        result.put("pageSize",pageSize);
        boolean flag=false;
        Integer count=categoryMapper.getCountBySubCategory(Integer.parseInt(categoryId));
        if(count==0){
            count=categoryMapper.getCountBySubCategory2(Integer.parseInt(categoryId));
            flag=true;
        }
        result.put("count",count);
        Integer pages= count/Integer.parseInt(pageSize);
        result.put("pages",pages);
        List<Goods> item=new LinkedList<>();
        List<Goods> goods=new LinkedList<>();
        Integer real_page=Integer.parseInt(page)-1;
        if (real_page>pages){
            result.put("item",item);
            return result;
        }
        else {
            if(flag){
                goods=goodsMapper.getGoodsBySubCategory2(Integer.parseInt(categoryId),sortField);
            }
            else {
                goods=goodsMapper.getGoodsBySubCategory(Integer.parseInt(categoryId),sortField);
            }
            if(real_page<pages) {
                item=goods.subList(real_page*Integer.parseInt(pageSize),(real_page+1)*Integer.parseInt(pageSize));
            }
            else {
                item=goods.subList(real_page*Integer.parseInt(pageSize),goods.size());
            }
            for(Goods i:item){
                i.setPicture(goodsMapper.getMainPicturesByGoodsId(Integer.parseInt(i.getId())).get(0));
            }
            result.put("items",item);
            return result;
        }

    }
}
