package com.rean.shopspring.service;

import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;

import java.util.List;

public interface UserService {
    User findByUserName(String username);

    void register(String username, String password,String email,String nickname,String receiver,String contact,String address);

    void addAddress(String receiver,String contact,String address, Integer id);

    List<Address> getAddress(Integer id);
}
