package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerOrderResponse {
    private Integer total;
    private List<Items> items;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Skus {
        private Integer id;
        private String image;
        private String name;
        private String attrsText;
        private Integer count;
        private Integer payMoney;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Items {
        private Integer id;
        private Integer order_id;
        private Integer orderState;
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
        private Timestamp createTime;
        private Integer countdown;
        private Skus skus;
        private Integer user_id;
    }
}
