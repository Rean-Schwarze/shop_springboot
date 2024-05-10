package com.rean.shopspring.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
public class Category {
    @Range(min=100,message = "参数错误")
    private Integer id;//主键ID
    private String name;
    private String picture;
    private Integer parent_id;
    @Valid
    private List<Category> children;
//    private List<Goods> goods;
}
