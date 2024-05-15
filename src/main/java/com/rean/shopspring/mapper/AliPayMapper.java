package com.rean.shopspring.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AliPayMapper {
    @Select("select appPrivateKey from alipay where type=#{type}")
    String getPrivateKey(String type);

    @Select("select alipayPublicKey from alipay where type=#{type}")
    String getPublicKey(String type);
}
