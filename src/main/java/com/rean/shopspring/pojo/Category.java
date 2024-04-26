package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class Category {
    @NotEmpty
    private String id;//主键ID
    @NotEmpty
    private String name;
    private String picture;
    private Integer parent_id;
    private List<Category> children;
//    private List<Goods> goods;
}
