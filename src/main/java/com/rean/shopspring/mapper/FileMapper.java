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

    @Insert("insert into main_pictures(address, goods_id) values (#{url},#{id})")
    void addMainPictures(String url,int id);

    @Insert("insert into detail_pictures(address, goods_id) values (#{url},#{id})")
    void addDetailPictures(String url,int id);

    @Update("update category set picture=#{url} where id=#{id}")
    void addCategoryPictures(String url,int id);

    @Update("update sub_category set picture=#{url} where id=#{id}")
    void addSubCategoryPictures(String url,int id);

    @Insert("insert into banner(imgUrl) value (#{url})")
    void addBannerPictures(String url);
}
