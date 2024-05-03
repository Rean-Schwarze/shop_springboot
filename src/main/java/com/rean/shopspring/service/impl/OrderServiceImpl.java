package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CartMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.OrderMapper;
import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Order;
import com.rean.shopspring.pojo.OrderItem;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CartMapper cartMapper;


//    提交订单
    @Override
    public Order postOrder(Map<String, Object> orderOption) {
//        获取用户id
        Map<String, Object> u= ThreadLocalUtil.get();
        int user_id= Integer.parseInt((String) u.get("id"));

//        获取订单参数
        int deliveryTimeType= (int) orderOption.get("deliveryTimeType");
        String buyerMessage= (String) orderOption.get("buyerMessage");

        int payType=(int) orderOption.get("payType");
        int payChannel=(int) orderOption.get("payChannel");
        String addressId= String.valueOf((int) orderOption.get("addressId"));
        List<Map<String,Object>> goods= (List<Map<String, Object>>) orderOption.get("goods");

        List<OrderItem> orderItemList =new ArrayList<>();

        int orderState=1;
//        设置订单创建时间、支付截止时间
        Timestamp createTime=new Timestamp(System.currentTimeMillis());
        // 创建Calendar对象，并设置时间为当前时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createTime.getTime());
        calendar.add(Calendar.MINUTE,30);
        Timestamp payLatestTime=new Timestamp(calendar.getTimeInMillis()); // 截止付款时间为订单创建时间+30min

        int poseFee=0;
        if ((int)orderOption.get("payType")!=0){
            poseFee=5;
        }
        int totalMoney=0;
        int totalNum=0;
        for(Map<String,Object> good:goods){
            int count= (int)good.get("count");
            Integer skuId= (Integer) good.get("skuId");

//            将订单中每项商品信息单独存储
            CartItem cartItem=cartMapper.getCartListByUserIdAndSkuId(user_id,skuId);
            OrderItem orderItem=new OrderItem();
            orderItem.setCount(count);
            orderItem.setSkuId(skuId);
            orderItem.setUser_id(user_id);
            orderItem.setGoods_id(Integer.parseInt(cartItem.getId()));
            orderItemList.add(orderItem);

//            计算总价
            totalNum+=count;
            String price=goodsMapper.getPriceBySkuId(skuId);
            totalMoney+=Integer.parseInt(price)*count;
            goodsMapper.updateSkuInventory(skuId, goodsMapper.getInventoryBySkuId(skuId)-count); // 修改库存

            cartMapper.deleteBySkuIdAndUserId(skuId,user_id); // 删除购物车中商品
        }
        int payMoney=totalMoney+poseFee;

//        添加订单到数据库
        orderMapper.addOrder(createTime,payType,orderState,payLatestTime,poseFee,payMoney,totalMoney,totalNum,
                payChannel,user_id,Integer.parseInt(addressId));

//        获取订单
        Order newOrder=orderMapper.getOrder(user_id,Integer.parseInt(addressId),payMoney);

//        绑定订单id，添加到数据库
        for(OrderItem orderItem: orderItemList){
            orderItem.setOrder_id(Integer.parseInt(newOrder.getId()));
            orderMapper.InsertOrderItem(orderItem.getOrder_id(),orderItem.getSkuId(),orderItem.getUser_id(),orderItem.getCount(),orderItem.getGoods_id());
        }

        return newOrder;
    }

    @Override
    public Order getOrder(int id){
        return orderMapper.getOrderById(id);
    }

//    获取特定用户订单总数
    @Override
    public int getUserOrderCounts(int user_id){
        return orderMapper.getUserOrderCounts(user_id);
    }

    //    获取特定用户订单总数（指定订单种类）
    @Override
    public int getUserOrderCountsByState(int user_id, int orderState){
        return orderMapper.getUserOrderCountsByState(user_id,orderState);
    }

//    获取特定用户的订单（指定范围以及订单状态，0为全部订单）
    @Override
    public List<Map<String,Object>> getUserOrder(int user_id,int start,int size,int orderState){
        List<Map<String,Object>> completeOrderList=new ArrayList<>();

        List<Order> orderList;
        if(orderState==0){
            orderList=orderMapper.getUserOrderByRange(user_id,start,size);
        }
        else{
            orderList=orderMapper.getUserOrderByRangeAndState(user_id, start, size, orderState);
        }
        for(Order order:orderList){
//            更新付款倒计时
            if(order.getCountdown()!=-1){
                Timestamp currentTime=new Timestamp(System.currentTimeMillis());
                Timestamp payLastTime=order.getPayLatestTime();
                if(currentTime.before(payLastTime)){
                    order.setCountdown((int) (payLastTime.getTime()-currentTime.getTime()));
                    orderMapper.updateOrderCountdownById(Integer.parseInt(order.getId()),order.getCountdown());
                }
                else{
                    order.setCountdown(-1);
                    orderMapper.updateOrderCountdownById(Integer.parseInt(order.getId()),-1);
                }
            }
//            封装返回结果
            Map<String,Object> fullOrder=new HashMap<>();
            fullOrder.put("id",order.getId());
            fullOrder.put("createTime",order.getCreateTime());
            fullOrder.put("countdown",order.getCountdown());
            fullOrder.put("postFee",order.getPostFee());
            fullOrder.put("payMoney",order.getPayMoney());
            fullOrder.put("orderState",order.getOrderState());

            List<Map<String,Object>> skus=new ArrayList<>();
            List<OrderItem> orderItemList=orderMapper.getOrderItemByUserIdAndOrderId(user_id, Integer.parseInt(order.getId()));
            for(OrderItem orderItem:orderItemList){
                Map<String,Object> sku=new HashMap<>();
                sku.put("id",orderItem.getSkuId());
                Goods goods=goodsMapper.getGoodsBySkuId(orderItem.getSkuId());
                sku.put("image",goods.getPicture());
                sku.put("name",goods.getName());
                sku.put("attrsText",goods.getDesc());
                int count=orderItem.getCount();
                sku.put("quantity", count);
                double price= Double.parseDouble(goodsMapper.getPriceBySkuId(orderItem.getSkuId()));
                sku.put("realPay", price*count);

                skus.add(sku);
            }
            fullOrder.put("skus",skus);

            completeOrderList.add(fullOrder);
        }

        return completeOrderList;
    }
}
