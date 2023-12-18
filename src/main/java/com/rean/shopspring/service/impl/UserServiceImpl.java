package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.UserMapper;
import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public void register(String username, String password,String email,String nickname,String receiver,String contact,String address) {
        // 加密密码
        String md5String= Md5Util.getMD5String(password);
        // 添加
        userMapper.add(username,md5String,email,nickname);
        User u=userMapper.findByUserName(username);
        addAddress(receiver,contact,address,u.getId());
    }

    @Override
    public void addAddress(String receiver, String contact, String address, Integer id) {
        Integer count= userMapper.getCountOfAddressByUserId(id);
        int isDefault=0;
        if (count==0){
            isDefault=1;
        }
        userMapper.addAddressByUserId(id,receiver,contact,address,isDefault);
    }

    @Override
    public List<Address> getAddress(Integer id){
        return userMapper.getAddressByUserId(id);
    }
}
