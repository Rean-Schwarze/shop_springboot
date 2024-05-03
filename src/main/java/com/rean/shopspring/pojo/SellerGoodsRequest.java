package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class SellerGoodsRequest {
    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "名称不能含有非法字符")
    @Size(max=45,message = "名称字数不应超过45")
    private String name;

    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "描述不能含有非法字符")
    @Size(max=512,message = "描述字数不应超过512")
    private String desc;

    @NotEmpty
    private Integer category;

    @NotEmpty
    private Integer subCategory;

    private Integer subCategory2;

    @NotNull
    private List<Specs> specs;

    @NotNull
    private List<Sku> skus;

    @JsonProperty("isPreSale")
    private boolean isPreSale;

    @JsonProperty("isNew")
    private boolean isNew;

    private String svdName;

    private Timestamp pubTime;

    @JsonProperty("isOnSale")
    private boolean isOnSale;
}
