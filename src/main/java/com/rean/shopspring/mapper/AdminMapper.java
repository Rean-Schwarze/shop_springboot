package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("select * from admin where name=#{name}")
    Admin findByName(String name);
}
