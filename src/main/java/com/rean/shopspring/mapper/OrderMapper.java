package com.rean.shopspring.mapper;

import com.rean.shopspring.pojo.Order;
import com.rean.shopspring.pojo.OrderItem;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderMapper {

//    ------------------------
//    获取相关
//    ------------------------

    @Select("select * from `order` where id=#{id}")
    Order getOrderById(int id);

    @Select("select payMoney from `order` where id=#{id}")
    int getPayMoneyById(int id);

    @Select("select orderState from `order` where id=#{id}")
    int getOrderStateById(int id);

//    查询特定用户订单总数
    @Select("select count(*) from `order` where user_id=#{user_id}")
    int getUserOrderCounts(int user_id);

    //    查询特定用户订单总数（指定订单类型）
    @Select("select count(*) from `order` where user_id=#{user_id} and orderState=#{orderState}")
    int getUserOrderCountsByState(int user_id, int orderState);

//    查询用户特定范围内的订单（指定订单状态）（从新到旧排序）
    @Select("select * from `order` where user_id=#{user_id} and orderState=#{orderState} " +
            "order by createTime desc limit #{start},#{size}")
    List<Order> getUserOrderByRangeAndState(int user_id,int start,int size,int orderState);

//    查询用户特定范围内的订单（全部订单）（从新到旧排序）
    @Select("select * from `order` where user_id=#{user_id} " +
            "order by createTime desc limit #{start},#{size}")
    List<Order> getUserOrderByRange(int user_id,int start,int size);

    @Select("select * from order_item where user_id=#{user_id} and order_id=#{order_id}")
    List<OrderItem> getOrderItemByUserIdAndOrderId(int user_id, int order_id);

//    ------------------------
//    添加相关
//    ------------------------

    @Insert("insert into `order`(createTime,payType,orderState,payLatestTime,postFee,payMoney,totalMoney," +
            "totalNum,payChannel,user_id,user_address_id) values (#{createTime},#{payType},#{orderState},#{payLatestTime},#{postFee}," +
            "#{payMoney},#{totalMoney},#{totalNum},#{payChannel},#{userId},#{userAddressId})")
    @Options(useGeneratedKeys = true,keyProperty = "id",keyColumn = "id")
    void addOrder(Order order);

    //    插入订单中每项商品的skuId
    @Insert("insert into order_item(order_id,skuId,user_id,count,goods_id,brand_id) values (#{orderId},#{skuId},#{userId},#{count},#{goodsId},#{brandId})")
    void addOrderItem(OrderItem orderItem);


//    ------------------------
//    修改相关
//    ------------------------

    //    更新订单状态
    @Update("update `order` set orderState=#{state} where id=#{id}")
    void updateOrderStateById(int id, int state);

    //    更新付款倒计时
    @Update("update `order` set countdown=#{countdown} where id=#{id}")
    void updateOrderCountdownById(int id, int countdown);
}
