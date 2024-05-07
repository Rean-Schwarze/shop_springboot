package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SellerSkuRequest {
    @NotNull
    private Integer goods_id;
    private List<Sku> skus;
}
