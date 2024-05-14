package com.rean.shopspring.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Brand {
    private Integer id;
    private String name;
    private String type;
    private String desc;
    private String place;
    @JsonProperty("isValid")
    private boolean isValid;
}
