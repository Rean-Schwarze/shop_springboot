package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
public class SellerRegisterRequest {

    private Integer id;

    @NotEmpty(message = "请输入用户名")
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "用户名不能含有非法字符")
    private String name;//用户名

    @NotEmpty(message = "请输入密码")
    @Size(min=6,max=14,message = "密码长度应为6~14字符")
    private String password;//密码

    private String md5Password;

    private String avatar;

    @NotNull
    @Range(min=100000, message = "参数错误")
    private Integer brandId;

    @Valid
    private List<SellerCategory> category;

    @JsonProperty("isMaster")
    private Boolean isMaster;
}
