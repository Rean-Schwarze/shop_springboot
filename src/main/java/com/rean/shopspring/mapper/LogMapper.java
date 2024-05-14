package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Log;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface LogMapper {

//    ---------------------------
//    添加相关
//    ---------------------------

    @Insert("insert into log(userType, id, time, ip, type, value) VALUES (#{userType},#{id},#{time},#{ip},#{type},#{value})")
    void addLog(Log log);

//    ---------------------------
//    获取相关
//    ---------------------------

    // 获取用户浏览日志总数
    @Select("select count(*) from log where (type='tp' or type='buy') and value like #{likeString}")
    Integer getLogTpAndBuyCount(String likeString);

    // 获取用户浏览日志
    @Select("select * from log where (type='tp' or type='buy') and value like #{likeString} order by time desc limit #{start},#{pageSize}")
    List<Log> getLogTpAndBuy(String likeString, Integer start, Integer pageSize);
}
