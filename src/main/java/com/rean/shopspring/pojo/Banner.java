package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class Banner {
    @NotEmpty
    private String id;
    private String imgUrl;
    private String hrefUrl;
    private String type;
}
