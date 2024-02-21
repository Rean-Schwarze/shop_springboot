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
    @Size(min=4,max=16,message = "密码长度应为6~14字符")
    private String password;//密码

    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;//昵称

    @NotEmpty(message = "请输入邮箱地址")
    @Email(message = "请填写正确的邮箱地址")
    private String email;//邮箱

    @NotEmpty(message = "请输入手机号")
    @Pattern(regexp = "^\\d{11}$", message = "请填写正确的手机号")
    private String phone;

    @NotEmpty(message = "请输入收件人姓名")
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "收货人姓名不能含有非法字符")
    private String receiver;

    @NotEmpty(message = "请输入收货人手机号")
    @Pattern(regexp = "^\\d{11}$", message = "请填写正确的手机号")
    private String contact;

    @NotEmpty(message = "请输入收货地址（区域）")
    @Pattern(regexp = "[^^#*%&',;=?$\\x22]+", message = "收货地址（区域）不能含有非法字符")
    private String region;

    @NotEmpty(message = "请输入收货地址（详细）")
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "收货地址（详细）不能含有非法字符")
    private String address;
}
