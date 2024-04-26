package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller_categories {
    private Integer sellerId;
    private Integer categoryId;
    private Integer subCategoryId;
    @JsonProperty("isAllSub")
    private boolean isAllSub;
}
