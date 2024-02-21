package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserModifyInfoRequest {
    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "用户名不能含有非法字符")
    private String account;

    @NotEmpty
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "昵称不能含有非法字符")
    private String nickname;
}
