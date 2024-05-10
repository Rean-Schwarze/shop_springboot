package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegisterRequest {
    @NotEmpty(message = "请输入用户名")
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "用户名不能含有非法字符")
    private String account;//用户名

    @NotEmpty(message = "请输入密码")
    @Size(min=6,max=14,message = "密码长度应为6~14字符")
    private String password;//密码

    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;//昵称

    @NotEmpty(message = "请输入邮箱地址")
    @Email(message = "请填写正确的邮箱地址")
    private String email;//邮箱

    @NotEmpty(message = "请输入手机号")
    @Pattern(regexp = "^\\d{11}$", message = "请填写正确的手机号")
    private String phone;
}
