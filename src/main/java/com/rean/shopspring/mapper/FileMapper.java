package com.rean.shopspring.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface FileMapper {
    @Select("select id from goods where svd_name=#{name}")
    int findGoodsIdByName(String name);

    @Select("select id from category where name=#{name}")
    int findCategoryIdByName(String name);

    @Select("select id from sub_category where name=#{name}")
    int findSubCategoryIdByName(String name);

    @Insert("insert into main_pictures(address, goods_id, add_seller) values (#{url},#{goods_id},#{seller_id})")
    void addGoodsMainPictures(String url,Integer goods_id,Integer seller_id);

    @Insert("insert into detail_pictures(address, goods_id, add_seller) values (#{url},#{goods_id},#{seller_id})")
    void addGoodsDetailPictures(String url,Integer goods_id,Integer seller_id);

    @Update("update category set picture=#{url} where id=#{id}")
    void addCategoryPictures(String url,int id);

    @Update("update sub_category set picture=#{url} where id=#{id}")
    void addSubCategoryPictures(String url,int id);

    @Insert("insert into banner(imgUrl) value (#{url})")
    void addBannerPictures(String url);

    @Select("select accessKeyId from oss_ackey_value where ram=#{ram}")
    String getACCESSKEYIDbyRamName(String ram);

    @Select("select accessKeySecret from oss_ackey_value where ram=#{ram}")
    String getACCESSKEYSECRETbyRamName(String ram);
}
