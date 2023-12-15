package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

@Mapper
public interface GoodsMapper {

    @Select("select * from goods where id=#{id}")
    @Results({
            @Result(column = "brand_id",property = "brand_id",jdbcType = JdbcType.BIGINT),
            @Result(column = "category_id",property = "category_id",jdbcType = JdbcType.BIGINT),
            @Result(column = "sub_category_id",property = "sub_category_id",jdbcType = JdbcType.BIGINT),
            @Result(column = "sub_category_id2",property = "sub_category_id2",jdbcType = JdbcType.BIGINT)

    })
    Goods getGoodsById(Integer id);

    @Select("select * from brand where id=#{id}")
    Brand getBrandById(Integer id);

    @Select("select * from main_pictures where goods_id=#{id}")
    List<String> getMainPicturesByGoodsId(Integer id);

    @Select("select * from specs where id=(select specs_id from goods_specs where goods_id=#{id})")
    List<Specs> getSpcesByGoodsId(Integer id);

    @Select("select * from specs_values where specs_id=#{id}")
    List<Spec_values> getSpcesValueBySpecsId(String id);

    @Select("select * from sku where id in (select sku_id from specs_values_sku where goods_id=#{id})")
    List<Sku> getSkuByGoodsId(String id);

    @Select("select * from specs_values where id=(select specs_values_id from specs_values_sku where sku_id=#{id})")
    List<Spec_values> getSkuSpecBySkuId(String id);

    @Select("select name from specs where id=(select specs_id from specs_values where id=#{id})")
    String getSpecNameBySpceValueId(Integer id);
    @Select("select address from detail_pictures where goods_id=#{id}")
    List<String> getDetailPicturesByGoodsId(Integer id);

    @Select("select * from properties where goods_id=#{id}")
    List<Property> getDetailPropertiesByGoodsId(Integer id);
}
