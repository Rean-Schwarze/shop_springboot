package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogTpAndBuyResponse {
    private Integer total;
    private List<Log> logs;
}
