package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderPre {
    @NotEmpty
    private int id;
    private int totalPrice;
    private int goodsCount;
    private boolean status;
    private int paytype;
    private int totalPayPrice;
    private int postFee;
}
