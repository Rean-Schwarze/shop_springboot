package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Spec_values {
    private Integer id;

    @NotEmpty
    @Size(min=1,max=45,message = "详细规格名长度应为1~45")
    private String name;

    private String picture;

    private String desc;

    private Integer specsId;

    private Integer goodsId;
}
