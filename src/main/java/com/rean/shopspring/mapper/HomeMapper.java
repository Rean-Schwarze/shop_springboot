package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Banner;
import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Goods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HomeMapper {
    @Select("select * from category")
    List<Category> getAllCategories();

    @Select("select * from sub_category")
    List<Category> getAllSubCategories();

    @Select("select * from category where id=#{id}")
    Category getCategoryById(Integer id);

    @Select("select * from sub_category where parent_id=#{id};")
    List<Category> getSubCategoriesByParentId(int id);

    @Select("select * from goods where category_id=#{id} limit 8")
    List<Goods> get8GoodsByCategory(Integer id);

    @Select("select * from goods where sub_category_id=#{id}")
    List<Goods> getGoodsBySubCategoryId(Integer id);

    @Select("select * from goods where sub_category_id2=#{id}")
    List<Goods> getGoodsBySubCategoryId2(Integer id);

    @Select("select address from main_pictures where goods_id=#{id} limit 1")
    String getGoodsPicturesById(Integer id);

    @Select("select * from goods where is_new=1 limit #{limit}")
    List<Goods> getNewGoods(Integer limit);

    @Select("select * from banner")
    List<Banner> getBanners();
}
