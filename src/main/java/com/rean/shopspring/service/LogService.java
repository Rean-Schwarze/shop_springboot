package com.rean.shopspring.service;

public interface LogService {
    void logLogin(String userType, Integer id, String ip, String type);
}
