package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class Goods {
    @NotEmpty
    private String id;
    private String name;
    private String price;
    private String oldPrice;
    private String desc;
    private Integer inventory;
    private int brand_id;
    private Integer salesCount;
    private Integer commentCount;
    private Integer collectCount;
    private int isPreSale;
    private int category_id;
    private int sub_category_id;
    private int sub_category_id2;
    private boolean is_new;
    private String picture;
    private Brand brand;
    private List<String> mainPictures;
    private List<Specs> specs;
    private List<Sku> skus;
    private Detail details;
}
