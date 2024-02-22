package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.pojo.UserModifyInfoRequest;

import java.io.InputStream;
import java.util.List;

public interface UserService {
    User findByUserName(String username);

    User findByPhone(String phone);

    User findByEmail(String email);

    boolean isEmailAndPhoneExists(String email,String phone);

    void register(String username,String phone, String password,String email,String nickname);

    //    添加收货地址
    void addAddress(Address address);
    void addAddress(String receiver,String contact,String address, Integer id,String fullLocation);

    List<Address> getAddress(Integer id);

//    用户登录后，从ThreadLocalUtil中获取用户id
    int getUserIdIfLogin();

    //    上传用户头像
    String uploadUserAvatar(String uploadFilename, InputStream in) throws Exception;

//    修改基本信息
    void modifyBasicInfo(UserModifyInfoRequest userModifyInfoRequest);

//    修改收货地址
    void modifyAddress(Address address);
}
