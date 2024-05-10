package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.LogMapper;
import com.rean.shopspring.pojo.LogLogin;
import com.rean.shopspring.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogMapper logMapper;

    @Override
    public void logLogin(String userType, Integer id, String ip, String type){
        Timestamp time=new Timestamp(System.currentTimeMillis());
        LogLogin log=new LogLogin(userType,id,time,ip,type);
        logMapper.addLogLogin(log);
    }
}
