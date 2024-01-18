package com.rean.shopspring.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Order {
    private String id;
    private String createTime;
    private int orderState;
    private Timestamp payLatestTime;
    private int postFee;
    private int payMoney;
    private int totalMoney;
    private int totalNum;
    private int payChannel;
    private int payType;
    private String payTime;
    private String consignTime;
    private String endTime;
    private String closeTime;
    private String evaluationTime;
    private int countdown;
    private int user_id;
}
