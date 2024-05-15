package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Integer id;
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    private Integer orderState;
    private Timestamp payLatestTime;
    private Integer postFee;
    private Integer payMoney;
    private Integer totalMoney;
    private Integer totalNum;
    private Integer payChannel;
    private Integer payType;
    private Timestamp payTime;
    private Timestamp consignTime;
    private Timestamp endTime;
    private Timestamp closeTime;
    private Timestamp evaluationTime;
    private Integer countdown;
    private Integer userId;
    private Integer userAddressId;
}
