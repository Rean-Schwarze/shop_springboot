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
            "(#{receiver},#{contact},#{address},#{isDefault},#{user_id},#{region})")
    void addAddressByUserId(Integer user_id,String receiver,String contact,String address,int isDefault,String region);

    @Select("select * from user_address where user_id=#{id}")
    List<Address> getAddressByUserId(Integer id);

    @Update("update user_address set isDefault=0 where user_id=#{user_id}")
    void updateAddressAllNotDefaultByUserId(Integer user_id);

    @Update("update user_address set isDefault=1 where user_id=#{user_id} and id=#{add_id}")
    void updateAddressDefaultById(Integer user_id, Integer add_id);

    @Update("update user_address set receiver=#{receiver}, contact=#{contact}, region=#{region}, address=#{address}, isDefault=#{isDefault} where id=#{add_id} and user_id=#{user_id}")
    void updateAddressById(Integer user_id, Integer add_id,String receiver,String contact,String region,String address,int isDefault);

    @Select("select email from user where id=#{id}")
    String getEmailByUserId(Integer id);

    @Update("update user set avatar=#{url} where id=#{user_id}")
    void updateUserAvatar(int user_id,String url);

    @Select("select avatar from user where id=#{user_id}")
    String getAvatarByUserId(int user_id);

    @Update("update user set username=#{account}, nickname=#{nickname} where id=#{user_id}")
    void updateUserBasicInfo(String account, String nickname, int user_id);
}
