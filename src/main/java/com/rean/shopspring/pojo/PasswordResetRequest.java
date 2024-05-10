package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {
    @NotNull
    @Range(min=1,message = "参数错误")
    private Integer id;

//    @NotEmpty(message = "请输入旧密码")
//    @Size(min=6,max=14,message = "密码长度应为6~14字符")
//    private String oldPassword;

    @NotEmpty(message = "请输入新密码")
    @Size(min=6,max=14,message = "密码长度应为6~14字符")
    private String newPassword;
}
