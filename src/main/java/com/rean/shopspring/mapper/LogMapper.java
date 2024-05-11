package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper {
    @Insert("insert into log(userType, id, time, ip, type, value) VALUES (#{userType},#{id},#{time},#{ip},#{type},#{value})")
    void addLog(Log log);
}
