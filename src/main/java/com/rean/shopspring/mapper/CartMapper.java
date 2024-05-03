package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CartMapper {

    @Insert("insert into cart_list(price,count,id,skuId,user_id) values (#{price},#{count},#{goods_id},#{skuId},#{user_id})")
    void addCartList(Double price, Integer count, Integer goods_id, Integer skuId, Integer user_id);

//    获取某用户购物车中所有商品
    @Select("select * from cart_list where user_id =#{id}")
    List<CartItem> getCartListByUserId(Integer user_id);

//    获取特定用户特定商品（创建订单时用）
    @Select("select * from cart_list where user_id=#{user_id} and skuId=#{skuId}")
    CartItem getCartListByUserIdAndSkuId(int user_id, Integer skuId);

    @Select("select count(*) from cart_list where skuId=#{skuId} and user_id=#{user_id}")
    Integer getSkuIdCount(Integer skuId, Integer user_id);

    @Update("update cart_list set count=#{count} where skuId=#{skuId}")
    void updateCountBySkuId(Integer skuId, Integer count);

    @Select("select goods_id from sku where id=#{skuId}")
    Integer getGoodsIdBySkuId(Integer skuId);

    @Select("select price from sku where id=#{skuId}")
    Double getPriceBySkuId(Integer skuId);

    @Delete("delete from cart_list where skuId=#{skuId} and user_id=#{user_id}")
    void deleteBySkuIdAndUserId(Integer skuId,Integer user_id);

}
