package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.SellerCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

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

    // 获取销售人员负责分类
    @Select("select * from category where id=#{id}")
    SellerCategory getSellerCategoryById(Integer id);

    //    获取子分类（传入：子分类id）
    @Select("select * from sub_category where id=#{id}")
    Category getSubCategoryById(Integer id);

    //    获取子分类（传入：父分类id）
    @Select("select * from sub_category where parent_id=#{parent_id}")
    List<Category> getSubCategoryByParentId(Integer parent_id);

    //    获取子分类id（传入：父分类id）
    @Select("select id from sub_category where parent_id=#{parent_id}")
    List<Integer> getSubCategoryIdByParentId(Integer parent_id);

    @Select("select count(*) from goods where sub_category_id=#{id}")
    Integer getCountBySubCategory(Integer id);

    @Select("select count(*) from goods where sub_category_id2=#{id}")
    Integer getCountBySubCategory2(Integer id);

}
