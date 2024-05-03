package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class Specs {
    private String id;

    @NotEmpty
    @Size(min=1,max=45,message = "规格名长度应为1~45")
    private String name;

    private Integer goodsId;

    @NotNull
    private List<Spec_values> values;
}
