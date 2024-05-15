package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper=false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HomeCategoryGoodsResponse extends Category {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GoodsLite{
        private Integer id;
        private String name;
        private String desc;
        private String picture;
        private String price;
    }
    private List<GoodsLite> goods;
}
