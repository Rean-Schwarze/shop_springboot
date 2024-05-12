package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CartMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.OrderMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

import static com.rean.shopspring.utils.GoodsUtil.getAttrsText;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CartMapper cartMapper;

    private String joinAttrsText(Integer skuId){
        return getAttrsText(skuId, goodsMapper);
    }

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
        Integer addressId= (Integer) orderOption.get("addressId");
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

        int postFee=0;
        if ((int)orderOption.get("payType")!=0){
            postFee=5;
        }
        int totalMoney=0;
        int totalNum=0;
        for(Map<String,Object> good:goods){
            int count= (int)good.get("count");
            Integer skuId= (Integer) good.get("skuId");

            // 计算总价
            totalNum+=count;
            String price=goodsMapper.getPriceBySkuId(skuId);
            int payMoneySingle=Integer.parseInt(price)*count;
            totalMoney+=payMoneySingle;
            goodsMapper.updateSkuInventory(skuId, goodsMapper.getInventoryBySkuId(skuId)-count); // 修改库存

//            将订单中每项商品信息单独存储
            CartItem cartItem=cartMapper.getCartListByUserIdAndSkuId(user_id,skuId);
            OrderItem orderItem=new OrderItem(null,skuId,user_id,null,cartItem.getId(),count,payMoneySingle,
                    null,goodsMapper.getBrandIdByGoodsId(cartItem.getId()),1,createTime);
            orderItemList.add(orderItem);

            cartMapper.deleteBySkuIdAndUserId(skuId,user_id); // 删除购物车中商品
        }
        int payMoney=totalMoney+postFee;

        // 添加订单到数据库
        Order order=new Order(null,createTime,orderState,payLatestTime,postFee,payMoney,totalMoney,totalNum,
                payChannel,payType,null,null,null,null,null,
                null,user_id,addressId);

        orderMapper.addOrder(order);

//        绑定订单id，添加到数据库
        for(OrderItem orderItem: orderItemList){
            orderItem.setOrderId(order.getId());
            orderMapper.addOrderItem(orderItem);
        }

        return order;
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
                    orderMapper.updateOrderCountdownById(order.getId(),order.getCountdown());
                }
                else{
                    order.setCountdown(-1);
                    orderMapper.updateOrderCountdownById(order.getId(),-1);
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
            List<OrderItem> orderItemList=orderMapper.getOrderItemByUserIdAndOrderId(user_id, order.getId());
            for(OrderItem orderItem:orderItemList){
                Map<String,Object> sku=new HashMap<>();
                sku.put("id",orderItem.getSkuId());
                Integer goods_id=goodsMapper.getGoodsIdBySkuId(orderItem.getSkuId());
                sku.put("image",goodsMapper.getMainPicturesByGoodsId(goods_id));
                sku.put("name",goodsMapper.getNameByGoodsId(goods_id));
                sku.put("attrsText",joinAttrsText(orderItem.getSkuId()));
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

    // 修改订单状态
    public void updateOrderState(Integer order_id,Integer orderState){
        orderMapper.updateOrderStateById(order_id,orderState);
        orderMapper.updateOrderItemStateByOrderId(order_id,orderState);
        // 已付款，更新sku
        if(orderState==2){
            List<OrderItem> orderItemList=orderMapper.getOrderItemByOrderId(order_id);
            for(OrderItem orderItem:orderItemList){
                // 是否考虑上锁？
                Sku sku=goodsMapper.getSkuById(orderItem.getSkuId());
                Integer count=sku.getSalesCount()+orderItem.getCount();
                Integer volume= sku.getSalesVolume()+orderItem.getCount()*Integer.parseInt(sku.getPrice());
                goodsMapper.updateSkuCountAndVolume(orderItem.getSkuId(),count,volume);
            }
        }
    }
}
