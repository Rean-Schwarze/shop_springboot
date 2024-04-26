package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seller {
    @NotNull
    private Integer id;//主键ID

    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "不能含有非法字符")
    private String name;//用户名

    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//密码

    private String avatar;//用户头像地址

    private Integer brand_id;//品牌id

    @JsonProperty("isMaster")
    private boolean isMaster;
}
