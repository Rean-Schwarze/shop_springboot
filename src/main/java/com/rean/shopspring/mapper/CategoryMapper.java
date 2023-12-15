package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CategoryMapper {

    @Select("select name from category where id=(select parent_id from sub_category where id=#{id})")
    String getParentCategoryName(Integer id);

    @Select("select parent_id from sub_category where id=#{id}")
    String getParentCategoryId(Integer id);

    @Select("select name from sub_category where id=#{id}")
    String getSubCategoryNameById(Integer id);

    @Select("select * from category where id=#{id}")
    Category getCategoryById(Integer id);

    @Select("select * from sub_category where id=#{id}")
    Category getSubCategoryById(Integer id);

    @Select("select count(*) from goods where sub_category_id=#{id}")
    Integer getCountBySubCategory(Integer id);

    @Select("select count(*) from goods where sub_category_id2=#{id}")
    Integer getCountBySubCategory2(Integer id);


}
