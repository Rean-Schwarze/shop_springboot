package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Spec_values {
    @NotEmpty
    private String id;
    private String name;
    private String picture;
    private String desc;
}
