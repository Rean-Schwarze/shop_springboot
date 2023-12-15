package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Goods {
    @NotEmpty
    private String id;
    private String name;
    private String price;
    private String oldPrice;
    private String desc;
    private String inventory;
    private Integer brand_id;
    private String salesCount;
    private String commentCount;
    private String collectCount;
    private boolean isPreSale;
    private Integer category_id;
    private Integer sub_category_id;
    private Integer sub_category_id2;
    private boolean is_new;
    private String picture;
}
