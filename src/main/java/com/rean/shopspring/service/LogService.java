package com.rean.shopspring.service;

public interface LogService {
    void logLogin(String userType, Integer id, String ip, String type);

    void logTp(Integer id, String ip, String value);

    void logSeller(Integer id, String ip, String type, String value);

    void logAdmin(Integer id, String ip, String type, String value);
}
