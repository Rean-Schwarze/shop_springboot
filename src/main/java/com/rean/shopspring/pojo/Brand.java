package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Brand {
    @NotEmpty
    private String id;
    private String name;
    private String type;
    private String desc;
    private String place;
}
