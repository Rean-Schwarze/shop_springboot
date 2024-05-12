package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.LogMapper;
import com.rean.shopspring.pojo.Log;
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
        Log log=new Log(userType,id,time,ip,type,null);
        logMapper.addLog(log);
    }

    @Override
    public void logTp(Integer id, String ip, String value){
        Timestamp time=new Timestamp(System.currentTimeMillis());
        Log log=new Log("user",id,time,ip,"tp",value);
        logMapper.addLog(log);
    }

    @Override
    public void logSeller(Integer id, String ip, String type, String value){
        Timestamp time=new Timestamp(System.currentTimeMillis());
        Log log=new Log("seller",id,time,ip,type,value);
        logMapper.addLog(log);
    }

    @Override
    public void logAdmin(Integer id, String ip, String type, String value){
        Timestamp time=new Timestamp(System.currentTimeMillis());
        Log log=new Log("admin",id,time,ip,type,value);
        logMapper.addLog(log);
    }
}
