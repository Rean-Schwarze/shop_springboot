package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Admin;
import com.rean.shopspring.pojo.Brand;
import com.rean.shopspring.pojo.Seller;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {

//    -----------------------------
//    获取相关
//    -----------------------------
    @Select("select * from admin where name=#{name}")
    Admin findByName(String name);

    // 获取销售人员数量（全部）
    @Select("select count(*) from seller where isValid=#{isValid}")
    Integer getSellerCountAll(Boolean isValid);

    // 获取销售人员数量（指定品牌）
    @Select("select count(*) from seller where brand_id=#{brand_id} and isValid=#{isValid}")
    Integer getSellerCountByBrandId(Integer brand_id, Boolean isValid);

    // 获取销售人员列表（全部）
    @Select("select * from seller where isValid=#{isValid} limit #{start},#{pageSize}")
    List<Seller> getSellerAll(Integer start,Integer pageSize, Boolean isValid);

    // 获取销售人员列表（指定品牌）
    @Select("select * from seller where brand_id=#{brand_id} and isValid=#{isValid} limit #{start},#{pageSize}")
    List<Seller> getSellerByBrandId(Integer brand_id,Integer start,Integer pageSize, Boolean isValid);

    // 获取品牌信息
    @Select("select * from brand where id=#{id} and isValid=true")
    Brand getBrandById(Integer id);

    // 获取所有品牌
    @Select("select * from brand where isValid=true")
    List<Brand> getBrandsAll();

//    -----------------------------
//    修改相关
//    -----------------------------

    // 删除销售人员（伪）
    @Update("update seller set isValid=false where id=#{id}")
    void deleteSellerFake(Integer id);
}
