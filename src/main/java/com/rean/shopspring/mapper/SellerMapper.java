package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.Seller_categories;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SellerMapper {
    @Select("select * from seller where name=#{name}")
    Seller findByName(String name);

    @Select("select * from seller_categories where seller_id=#{seller_id}")
    List<Seller_categories> getSellCategory(Integer seller_id);

//    @Select("select * from category where id in (select * from )")
//    List<Category> getSellCategory(int seller_id);
}
