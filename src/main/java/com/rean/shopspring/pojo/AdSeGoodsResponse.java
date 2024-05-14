package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSeGoodsResponse {
    private Integer id;
    private String name;
    private String price;
    private String picture;
    private Integer totalStock;
    private Integer totalSales;
    private Integer totalVolume;
    private List<Specs> specs;
    private List<Sku> skus;
}
