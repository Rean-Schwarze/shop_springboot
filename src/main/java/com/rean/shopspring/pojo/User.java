package com.rean.shopspring.pojo;



import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
//lombok  在编译阶段,为实体类自动生成setter  getter toString
// pom文件中引入依赖   在实体类上添加注解
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @NotNull
    private Integer id;//主键ID
    @Pattern(regexp = "[^^#*%&',;=?\\s$\\x22]+", message = "不能含有非法字符")
    private String username;//用户名
    @JsonIgnore//让springmvc把当前对象转换成json字符串的时候,忽略password,最终的json字符串中就没有password这个属性了
    private String password;//密码

    @NotEmpty
    @Pattern(regexp = "^\\S{1,10}$")
    private String nickname;//昵称

    @NotEmpty
    @Email(message = "请填写正确的邮箱地址")
    private String email;//邮箱

    @Pattern(regexp = "^\\d{11}$", message = "请填写正确的手机号")
    private String phone;

    private String avatar;//用户头像地址

    private String name;
}
