package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class Sku {
    @NotEmpty
    private String id;
    private String skuCode;
    private String price;
    private String oldPrice;
    private Integer inventory;
    private List<Spec_sku> specs;
}
