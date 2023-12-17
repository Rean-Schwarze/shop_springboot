package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

    @Insert("insert into user(username,password,email,nickname,isadmin,isvalid)" +
            " values(#{username},#{password},#{email},#{nickname},0,1)")
    void add(String username, String password,String email,String nickname);

    @Select("select count(*) from user_address where user_id=#{id}")
    Integer getCountOfAddressByUserId(Integer id);

    @Insert("insert into user_address(receiver, contact, address, isDefault, user_id) VALUES (#{receiver},#{contact},#{address},#{isDefault},#{id})")
    void addAddressByUserId(Integer id,String receiver,String contact,String address,boolean isDefault);

    @Select("select * from user_address where user_id=#{id}")
    List<Address> getAddressByUserId(Integer id);

    @Select("select email from user where id=#{id}")
    String getEmailByUserId(Integer id);
}
