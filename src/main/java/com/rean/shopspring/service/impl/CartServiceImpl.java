package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CartMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Spec_values;
import com.rean.shopspring.pojo.User;
import com.rean.shopspring.service.CartService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CartMapper cartMapper;

    @Override
    public CartItem addCart(Integer id,String skuId,Integer count){
        Goods goods=goodsMapper.getGoodsById(id);

        Spec_values spec_values=goodsMapper.getSpecValuesBySkuId(skuId);
        String spec_name=goodsMapper.getSpecNameBySpceValueId(Integer.valueOf(spec_values.getId()));
        String attrsText=spec_name+"："+spec_values.getName();

        int postFee=8;
        String price= String.valueOf(cartMapper.getPriceBySkuId(skuId));
        Integer price_int=Integer.parseInt(price.split("\\.")[0]);
        String totalPrice= String.valueOf(price_int*count);
        String totalPayPrice= String.valueOf(Integer.parseInt(totalPrice)+postFee);

        goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(Integer.parseInt(goods.getId())));
        CartItem cartItem=new CartItem(goods.getId(),price,count,skuId,goods.getName(),
                attrsText,new ArrayList<>(),goods.getMainPictures().get(0),price,price,true,
                goods.getInventory(),false,false,postFee,price,totalPrice,totalPayPrice);

        Map<String, Object> u=ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));

        Integer SkuIdCount=cartMapper.getSkuIdCount(skuId);
        if(SkuIdCount==0){
            cartMapper.addOrderList(Double.valueOf(cartItem.getPrice()),count,Integer.parseInt(goods.getId()),skuId,user_id);
        }
        else{
            cartMapper.updateCountBySkuId(skuId,SkuIdCount+count);
        }
        return cartItem;
    }

    @Override
    public List<CartItem> getCartList() {
        Map<String, Object> u=ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));
        List<CartItem> cartItemList=cartMapper.getCartList(user_id);
        for(CartItem item:cartItemList){
            Goods goods=goodsMapper.getGoodsById(Integer.parseInt(item.getId()));
            item.setName(goods.getName());

            Spec_values spec_values=goodsMapper.getSpecValuesBySkuId(item.getSkuId());
            String spec_name=goodsMapper.getSpecNameBySpceValueId(Integer.valueOf(spec_values.getId()));
            String attrsText=spec_name+"："+spec_values.getName();
            item.setAttrsText(attrsText);

            goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(Integer.parseInt(goods.getId())));
            item.setPicture(goods.getMainPictures().get(0));

            item.setNowPrice(item.getPrice());
            item.setNowOriginalPrice(item.getPrice());

            item.setSelected(false);
            item.setStock(goods.getInventory());

            item.setEffective(false);

            item.setCollect(false);

            item.setPostFee(0);
        }

        return cartItemList;
    }

    @Override
    public void mergeCartList(List<Map<String, Object>> cartList) {
//        Map<String, Object> u=ThreadLocalUtil.get();
//        Integer user_id= (Integer) u.get("id");
        for(Map<String,Object> cart:cartList){
            Integer count= (Integer) cart.get("count");
            String skuId=(String) cart.get("skuId");
            Integer SkuIdCount=cartMapper.getSkuIdCount(skuId);
            if(SkuIdCount==0){
                Integer goods_id=cartMapper.getGoodsIdBySkuId(skuId);
                CartItem cartItem=addCart(goods_id,skuId,count);
            }
            else{
                cartMapper.updateCountBySkuId(skuId,SkuIdCount+count);
            }
        }
    }

    @Override
    public boolean deleteCartList(List<String> list){
        Map<String, Object> u=ThreadLocalUtil.get();
        Integer user_id=Integer.parseInt((String) u.get("id")) ;
        try{
            for(String id:list){
                cartMapper.deleteBySkuIdAndUserId(id,user_id);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
