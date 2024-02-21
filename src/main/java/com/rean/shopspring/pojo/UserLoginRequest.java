package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginRequest {
    @NotEmpty(message = "请输入邮箱/手机号")
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "账户不能含有非法字符")
    private String account;

    @NotEmpty(message = "请输入密码")
    @Size(min=4,max=16,message = "密码长度应为6~14字符")
    private String password;//密码
}
