package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.LogLogin;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    @Insert("insert into log_login(userType, id, time, ip, type) VALUES (#{userType},#{id},#{time},#{ip},#{type})")
    void addLogLogin(LogLogin log);
}
