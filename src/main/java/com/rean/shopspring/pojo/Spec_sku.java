package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Spec_sku {
    @NotEmpty
    private String name;
    @NotEmpty
    private String valueName;
}
