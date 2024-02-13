package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("select * from user where username=#{username}")
    User findByUserName(String username);

//    根据邮箱查询用户
    @Select("select * from user where email=#{email}")
    User findByEmail(String email);

//    根据手机号查询用户
    @Select("select * from user where phone=#{phone}")
    User findByPhone(String phone);
    @Insert("insert into user(username,phone,password,email,nickname,isadmin,isvalid)" +
            " values(#{username},#{phone},#{password},#{email},#{nickname},0,1)")
    void add(String username,String phone, String password, String email, String nickname);

    @Select("select count(*) from user_address where user_id=#{id}")
    Integer getCountOfAddressByUserId(Integer id);

    @Insert("insert into user_address(receiver, contact, address, isDefault, user_id, region) VALUES " +
            "(#{receiver},#{contact},#{address},#{isDefault},#{id},#{region})")
    void addAddressByUserId(Integer id,String receiver,String contact,String address,int isDefault,String region);

    @Select("select * from user_address where user_id=#{id}")
    List<Address> getAddressByUserId(Integer id);

    @Select("select email from user where id=#{id}")
    String getEmailByUserId(Integer id);

    @Update("update user set avatar=#{url} where id=#{user_id}")
    void updateUserAvatar(int user_id,String url);
}
