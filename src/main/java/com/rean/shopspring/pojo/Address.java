package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Address {
    private String receiver;
    private String contact;
    private String address;
    private boolean isDefault;
    private Integer user_id;
    @NotEmpty
    private Integer id;
}
