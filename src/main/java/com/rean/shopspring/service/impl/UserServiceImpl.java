package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.FileMapper;
import com.rean.shopspring.mapper.UserMapper;
import com.rean.shopspring.pojo.Address;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.pojo.UserModifyInfoRequest;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.AliOssUtil;
import com.rean.shopspring.utils.Md5Util;
import com.rean.shopspring.utils.ThreadLocalUtil;
import com.rean.shopspring.utils.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FileMapper fileMapper;
    @Override
    public User findByUserName(String username) {
        return userMapper.findByUserName(username);
    }

    @Override
    public User findByPhone(String phone) {return userMapper.findByPhone(phone);}

    @Override
    public User findByEmail(String email) {return userMapper.findByEmail(email);}

    @Override
    public boolean isEmailAndPhoneExists(String email,String phone){
        if(userMapper.findByEmail(email)==null){
            return userMapper.findByPhone(phone) == null;
        }
        else{
            return false;
        }
    }

    public boolean isNotEmpty(String s){
        return s != null && !(Objects.equals(s, ""));
    }

    @Override
    public void register(String username,String phone, String password,String email,String nickname) {
        // 加密密码
        String md5String= Md5Util.getMD5String(password);
        // 添加
        userMapper.add(username,phone,md5String,email,nickname);
        User u=userMapper.findByPhone(phone);
    }


    @Override
    public void addAddress(Address address){
        int user_id=getUserIdIfLogin();
        int isDefault=0;
        if(address.isDefault()){
            isDefault=1;
            userMapper.updateAddressAllNotDefaultByUserId(user_id);
        }
        userMapper.addAddressByUserId(user_id,address.getReceiver(),address.getContact(),address.getAddress(),isDefault,address.getRegion());
    }
//    废弃
//    添加收货地址（注册）
    @Override
    public void addAddress(String receiver, String contact, String address, Integer id,String region) {
        Integer count= userMapper.getCountOfAddressByUserId(id);
        int isDefault=0;
        if (count==0){
            isDefault=1;
        }
        userMapper.addAddressByUserId(id,receiver,contact,address,isDefault,region);
    }

    @Override
    public List<Address> getAddress(Integer id){
        return userMapper.getAddressByUserId(id);
    }

//    用户登录后，从ThreadLocalUtil中获取用户id
    @Override
    public int getUserIdIfLogin(){
        return UserUtil.getUserId();
    }

    @Override
    public String uploadUserAvatar(String uploadFilename, InputStream in) throws Exception {
        int user_id=getUserIdIfLogin();
        String ac_id=fileMapper.getACCESSKEYIDbyRamName("user");
        String ac_sec=fileMapper.getACCESSKEYSECRETbyRamName("user");

        String avatar=userMapper.getAvatarByUserId(user_id);
        if(!Objects.equals(avatar, "")){
            if(!(avatar ==null)){
                String oldPath=avatar.split(".com/")[1];
                boolean isSuccess=AliOssUtil.deleteFile(oldPath,ac_id,ac_sec);
            }
        }
        String url = AliOssUtil.uploadFile(uploadFilename,in,ac_id,ac_sec);
        userMapper.updateUserAvatar(user_id,url);
        return url;
    }

    @Override
    public void modifyBasicInfo(UserModifyInfoRequest userModifyInfoRequest){
        int user_id=getUserIdIfLogin();
        userMapper.updateUserBasicInfo(userModifyInfoRequest.getAccount(),userModifyInfoRequest.getNickname(),user_id);
    }

    @Override
    public void modifyAddress(Address address){
        int user_id=getUserIdIfLogin();
        int isDefault=0;
        if(address.isDefault()){
            isDefault=1;
            userMapper.updateAddressAllNotDefaultByUserId(user_id);
        }
        userMapper.updateAddressById(user_id,address.getId(),address.getReceiver(),address.getContact(),address.getRegion(),address.getAddress(),isDefault);
    }
}
