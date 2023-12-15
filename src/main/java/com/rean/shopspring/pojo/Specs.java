package com.rean.shopspring.pojo;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class Specs {
    @NotEmpty
    private String id;
    private String name;
    private List<Spec_values> values;
}
