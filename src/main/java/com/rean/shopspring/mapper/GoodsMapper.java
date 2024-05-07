package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.*;
import com.rean.shopspring.pojo.Property;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.type.JdbcType;

import java.sql.Timestamp;
import java.util.List;

@Mapper
public interface GoodsMapper {

//    ----------------------------------------
//    获取相关
//    ----------------------------------------

    @Select("select * from goods where id=#{id}")
//    @Results({
//            @Result(column = "brand_id",property = "brand_id",jdbcType = JdbcType.BIGINT),
//            @Result(column = "category_id",property = "category_id",jdbcType = JdbcType.BIGINT),
//            @Result(column = "sub_category_id",property = "sub_category_id",jdbcType = JdbcType.BIGINT),
//            @Result(column = "sub_category_id2",property = "sub_category_id2",jdbcType = JdbcType.BIGINT)
//
//    })
    Goods getGoodsById(Integer id);

    // 获取商品id（传入：品牌id、分类id）
    @Select("select id from goods where brand_id=#{brand_id} and category_id=#{category_id} and isValid=true")
    List<Integer> getGoodsIdByBrandAndCategory(Integer brand_id,Integer category_id);

    @Select("select * from brand where id=#{id}")
    Brand getBrandById(Integer id);

    @Select("select * from main_pictures where goods_id=#{id}")
    List<String> getMainPicturesByGoodsId(Integer id);

    @Select("select * from specs where goods_id=#{id}")
    List<Specs> getSpcesByGoodsId(Integer id);

    @Select("select * from specs_values where specs_id=#{id}")
    List<Spec_values> getSpcesValueBySpecsId(Integer id);

    // 废弃
    @Select("select * from specs_values where id=(select specs_values_id from specs_values_sku where sku_id=#{id})")
    Spec_values getSpecValuesBySkuId(Integer id);

    @Select("select price from sku where id=#{id}")
    String getPriceBySkuId(Integer id);

    // 获取goods表内商品价格
    @Select("select price from goods where id=#{id}")
    Integer getPriceByGoodsId(Integer id);

    @Select("select inventory from sku where id=#{id}")
    int getInventoryBySkuId(Integer id);

    @Select("select * from sku where goods_id=#{id}")
    List<Sku> getSkuByGoodsId(Integer id);

    @Select("select * from specs_values where id = (select specs_values_id from sku where id=#{id})")
    Spec_values getSkuSpecBySkuId(Integer id);

    @Select("select * from specs_values where id = (select specs_values_id2 from sku where id=#{id})")
    Spec_values getSkuSpecBySkuId_2(Integer id);

    @Select("select name from specs where id=(select specs_id from specs_values where id=#{id})")
    String getSpecNameBySpceValueId(Integer id);

    @Select("select address from detail_pictures where goods_id=#{id}")
    List<String> getDetailPicturesByGoodsId(Integer id);

    @Select("select * from properties where goods_id=#{id}")
    List<Property> getDetailPropertiesByGoodsId(Integer id);

    @Select("select * from goods where sub_category_id=#{id} and isValid=true order by #{mode}")
    List<Goods> getGoodsBySubCategory(Integer id, String mode);

    @Select("select * from goods where sub_category_id2=#{id} and isValid=true order by #{mode}")
    List<Goods> getGoodsBySubCategory2(Integer id, String mode);

//    获取商品（随机、指定limit）
    @Select("select id,name,`desc`,price from goods where isValid=true order by RAND() LIMIT #{limit}")
    List<Goods> getGoodsByRandom(Integer limit);

//    获取商品总数
    @Select("select count(*) from goods")
    int getGoodsCount();

//    根据skuId获取单个商品的封面、描述、名称
    @Select("select picture,name,`desc` from goods where id=(select goods_id from sku where id=#{skuId})")
    Goods getGoodsBySkuId(Integer skuId);

    // 根据skuId获取商品id
//    @Select("select goods_id from sku where id=#{skuId}")
//    Integer getGoodsIdBySkuId(Integer skuId);

//    ----------------------------------------
//    添加相关
//    ----------------------------------------

    // 添加商品（除图片&规格）
    // 返回自增id
    @Insert("insert into goods(name,`desc`,brand_id,category_id,sub_category_id,sub_category_id2," +
            "isPreSale,is_new,is_on_sale,svd_name,pubTime,add_seller) values(#{name},#{desc},#{brandId}," +
            "#{category},#{subCategory},#{subCategory2},#{isPreSale},#{isNew},#{isOnSale},#{svdName},#{pubTime},#{addSeller})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void addGoods(SellerGoodsRequest sellerGoodsRequest);

    // 添加规格
    @Insert("insert into specs(name,goods_id) values (#{name},#{goodsId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void addSpecs(Specs specs);

    // 添加具体规格
    @Insert("insert into specs_values(name,picture,`desc`,specs_id,goods_id) values (#{name},#{picture},#{desc}," +
            "#{specsId},#{goodsId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void addSpecsValues(Spec_values spec_values);

    // 添加sku
    @Insert("insert into sku (price,inventory,specs_values_id,specs_values_id2,goods_id) values (#{price},#{inventory}," +
            "#{specsValuesId},#{specsValuesId2},#{goodsId});")
    void addSku(Sku sku);

//    ----------------------------------------
//    修改相关
//    ----------------------------------------

    // 修改库存
    @Update("update sku set inventory=#{count} where id=#{id}")
    void updateSkuInventory(Integer id,int count);

    // 修改价格
    @Update("update sku set price=#{price}, old_price=#{oldPrice} where id=#{id}")
    void updateSkuPrice(Integer id, String price, String oldPrice);

    // 修改goods表内商品价格
    @Update("update goods set price=#{price}, oldPrice=#{oldPrice} where id=#{id}")
    void updateGoodsPrice(String price,String oldPrice, Integer id);

//    ----------------------------------------
//    删除相关
//    ----------------------------------------

    // 伪删除商品
    @Update("update goods set isValid=false where id=#{id}")
    void deleteGoodsFake(Integer id);

}
