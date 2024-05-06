package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Goods {
    private Integer id;
    private String name;
    private String price;
    private String oldPrice;
    private String desc;
    private Integer inventory;
    private Integer brandId;
    private Integer salesCount;
    private Integer commentCount;
    private Integer collectCount;
    private boolean isPreSale;
    private Integer categoryId;
    private Integer subCategoryId;
    private Integer subCategoryId2;
    private boolean isNew;
    private boolean isOnSale;
    private boolean isValid;
    private String picture;
    private Brand brand;
    private List<String> mainPictures;
    private List<Specs> specs;
    private List<Sku> skus;
    private Detail details;
    private List<Category> categories;
    private Timestamp pubTime;
}
