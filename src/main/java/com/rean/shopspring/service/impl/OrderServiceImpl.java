package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CartMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.OrderMapper;
import com.rean.shopspring.pojo.Order;
import com.rean.shopspring.service.OrderService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CartMapper cartMapper;


    @Override
    public Order postOrder(Map<String, Object> orderOption) {
        Map<String, Object> u= ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));

        int deliveryTimeType= (int) orderOption.get("deliveryTimeType");
        String buyerMessage= (String) orderOption.get("buyerMessage");

        int payType=(int) orderOption.get("payType");
        int payChannel=(int) orderOption.get("payChannel");
        String addressId= String.valueOf((int) orderOption.get("addressId"));
        List<Map<String,Object>> goods= (List<Map<String, Object>>) orderOption.get("goods");

        int orderState=1;
        Timestamp createTime=new Timestamp(System.currentTimeMillis());
        // 创建Calendar对象，并设置时间为当前时间戳
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(createTime.getTime());
        calendar.add(Calendar.MINUTE,30);
        Timestamp payLatestTime=new Timestamp(calendar.getTimeInMillis());

        int poseFee=8;
        int totalMoney=0;
        int totalNum=0;
        for(Map<String,Object> good:goods){
            int count= (int)good.get("count");
            String skuId= (String) good.get("skuId");
            totalNum+=count;
            String price=goodsMapper.getPriceBySkuId(skuId);
            totalMoney+=Integer.parseInt(price)*count;
            goodsMapper.updateSkuInventory(skuId, goodsMapper.getInventoryBySkuId(skuId)-count);
            cartMapper.deleteBySkuIdAndUserId(skuId,user_id);
        }
        int payMoney=totalMoney+poseFee;



        orderMapper.addOrder(createTime,payType,orderState,payLatestTime,poseFee,payMoney,totalMoney,totalNum,
                payChannel,user_id,Integer.parseInt(addressId));

        return orderMapper.getOrder(user_id,Integer.parseInt(addressId),payMoney);
    }

    @Override
    public Order getOrder(int id){
        return orderMapper.getOrderById(id);
    }
}
