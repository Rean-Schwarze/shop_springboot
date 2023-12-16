package com.rean.shopspring.pojo;

import lombok.Data;

@Data
public class Address {
    private String receiver;
    private String contact;
    private String address;
    private boolean isDefault;
    private Integer user_id;
}
