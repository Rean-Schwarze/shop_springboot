package com.rean.shopspring.utils;

import com.rean.shopspring.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class UserUtil {
    public static <T extends User> Map<String,Object> login(T loginUser, String type,StringRedisTemplate stringRedisTemplate){
        Map<String,Object> response=new HashMap<>();
        response.put("type",type);
        response.put("id",loginUser.getId());
        response.put("avatar",loginUser.getAvatar());
        if(Objects.equals(type, "user")){
            response.put("account",loginUser.getUsername());
            response.put("nickname",loginUser.getNickname());
            response.put("email",loginUser.getEmail());
        }
        else{
            response.put("name",loginUser.getName());
        }
        String token = JwtUtil.genToken(response);
        response.put("token",token);
        //把token存储到redis中
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.set(token,type+" id: "+loginUser.getId().toString(),12, TimeUnit.HOURS);

        return response;
    }

    public static Integer getUserId(){
        Map<String,Object> map= ThreadLocalUtil.get();
        return (Integer) map.get("id");
    }

    public static void logout(String token, StringRedisTemplate stringRedisTemplate){
        stringRedisTemplate.delete(token);
    }
}
