package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user(username,password,email,nickname,receiver,contact,address,isadmin,isvalid)" +
            " values(#{username},#{password},#{email},#{nickname},#{receiver},#{contact},#{address},0,1)")
    void add(String username, String password,String email,String nickname,String receiver,String contact,String address);
}
