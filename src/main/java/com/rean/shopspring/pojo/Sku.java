package com.rean.shopspring.pojo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
public class Sku {
    private Integer id;
    private String skuCode;
    @NotEmpty
    private String price;
    private String oldPrice;
    @NotNull
    @Range(min=1,max=9999,message = "库存数量应为1~9999")
    private Integer inventory;
    private Integer salesCount;
//    private Integer oldSalesCount;
    private Integer salesVolume;
    private Integer specsValuesId;
    private Integer specsValuesId2;
    private Integer goodsId;
    private String attrsText;
    @NotNull
    @Valid
    private List<Spec_sku> specs;
}
