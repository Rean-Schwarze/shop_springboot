package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Address {
    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "不能含有非法字符")
    private String receiver;

    @NotEmpty
    @Pattern(regexp = "^\\d{11}$", message = "请填写正确的手机号")
    private String contact;

    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?$\\x22]+", message = "不能含有非法字符")
    private String region;

    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "不能含有非法字符")
    private String address;

    @JsonProperty("isDefault")
    private boolean isDefault;
//    private Integer user_id;
//    @NotEmpty
    private Integer id;
}
