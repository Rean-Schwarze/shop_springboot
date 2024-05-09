package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminSellerResponse {
    private Integer id;
    private String name;
    private String avatar;
    @Data
    @AllArgsConstructor
    public static class Brand{
        private Integer id;
        private String name;
    }
    private Brand brand;
    private List<SellerCategory> category;
    @JsonProperty("isValid")
    private boolean isValid;
    @JsonProperty("isMaster")
    private boolean isMaster;
}
