package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.CartItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Insert("insert into order_list(price,count,id,skuId,user_id) values (#{price},#{count},#{goods_id},#{skuId},#{user_id})")
    void addOrderList(Double price,Integer count,Integer goods_id,String skuId,Integer user_id);

    @Select("select * from order_list where user_id =#{id}")
    List<CartItem> getCartList(Integer user_id);

    @Select("select count(*) from order_list where skuId=#{skuId}")
    Integer getSkuIdCount(String skuId);

    @Update("update order_list set count=#{count} where skuId=#{skuId}")
    void updateCountBySkuId(String skuId, Integer count);

    @Select("select goods_id from specs_values_sku where sku_id=#{skuId}")
    Integer getGoodsIdBySkuId(String skuId);

    @Select("select price from sku where id=#{skuId}")
    Double getPriceBySkuId(String skuId);

    @Delete("delete from order_list where skuId=#{skuId}")
    void deleteBySkuId(String skuId);
}
