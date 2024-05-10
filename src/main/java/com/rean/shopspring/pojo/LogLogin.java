package com.rean.shopspring.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogLogin {
    private String userType;
    private Integer id;
    private Timestamp time;
    private String ip;
    private String type;
}