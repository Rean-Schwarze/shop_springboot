package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTpRequest {
    @NotNull
    private Integer userId;
    @NotNull
    @Range(min=1000000,message = "参数错误")
    private Integer goodsId;
    @NotNull
    @Range(min=0,message = "参数错误")
    private Integer time;
    private String type;
}
