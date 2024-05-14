package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Log;

import java.util.List;

public interface LogService {
    void logLogin(String userType, Integer id, String ip, String type);

    void logTp(Integer id, String ip, String value);

    void logBuy(Integer id, String ip,String type, String value);

    void logSeller(Integer id, String ip, String type, String value);

    void logAdmin(Integer id, String ip, String type, String value);

    Integer getLogTpAndBuyCount(Integer goods_id);

    List<Log> getLogTpAndBuy(Integer goods_id, Integer start, Integer pageSize);
}
