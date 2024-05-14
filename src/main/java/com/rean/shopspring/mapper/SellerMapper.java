package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Category;
import com.rean.shopspring.pojo.Seller;
import com.rean.shopspring.pojo.SellerRegisterRequest;
import com.rean.shopspring.pojo.Seller_categories;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SellerMapper {

//    ------------------------------
//    获取相关
//    ------------------------------

    @Select("select * from seller where name=#{name}")
    Seller findByName(String name);

    @Select("select * from seller where id=#{id}")
    Seller findById(Integer id);

    @Select("select * from seller_categories where seller_id=#{seller_id}")
    List<Seller_categories> getSellCategory(Integer seller_id);

    @Select("select brand_id from seller where id=#{seller_id}")
    Integer getSellBrandId(Integer seller_id);

//    ------------------------------
//    添加相关
//    ------------------------------

    // 销售人员账号注册
    @Insert("insert into seller(name,password,avatar,brand_id) values (#{name},#{md5Password},#{avatar},#{brandId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void sellerRegister(SellerRegisterRequest request);

    // 绑定负责分类
    @Insert("insert into seller_categories(seller_id,category_id,sub_category_id,is_all_sub) values (#{sellerId},#{categoryId},#{subCategoryId},#{isAllSub})")
    void bindCategory(Seller_categories categories);

//    ------------------------------
//    修改相关
//    ------------------------------

    // 修改密码
    @Update("update seller set password=#{newPassword} where id=#{id}")
    void updatePassword(Integer id,String newPassword);
}
