package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;

@Mapper
public interface OrderMapper {
    @Insert("insert into `order`(createTime,payType,orderState,payLatestTime,postFee,payMoney,totalMoney," +
            "totalNum,payChannel,user_id,user_address_id) values (#{createTime},#{payType},#{orderState},#{payLastTime},#{postFee}," +
            "#{payMoney},#{totalMoney},#{totalNum},#{payChannel},#{user_id},#{user_address_id})")
    void addOrder(Timestamp createTime, int payType, int orderState, Timestamp payLastTime, int postFee, int payMoney,
                  int totalMoney, int totalNum, int payChannel, int user_id, int user_address_id);

    @Select("select * from `order` where user_id=#{user_id} and user_address_id=#{user_address_id} and payMoney=#{payMoney} and orderState=1")
    Order getOrder(int user_id,int user_address_id,int payMoney);

    @Select("select * from `order` where id=#{id}")
    Order getOrderById(int id);

    @Select("select payMoney from `order` where id=#{id}")
    int getPayMoneyById(int id);

    @Select("select orderState from `order` where id=#{id}")
    int getOrderStateById(int id);

    @Update("update `order` set orderState=#{state} where id=#{id}")
    void updateOrderStateById(int id, int state);
}
