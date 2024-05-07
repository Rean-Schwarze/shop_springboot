package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CartMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.pojo.CartItem;
import com.rean.shopspring.pojo.Goods;
import com.rean.shopspring.pojo.Spec_values;
import com.rean.shopspring.service.CartService;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.rean.shopspring.utils.GoodsUtil.getAttrsText;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private UserService userService;

    // 获取描述规格属性的文字（传入：skuId）
    //（例：颜色分类：浅驼色 尺码：M[85-110斤]）
    private String joinAttrsText(Integer skuId){
        return getAttrsText(skuId, goodsMapper);
    }

    @Override
    public CartItem addCart(Integer id,Integer skuId,Integer count){
        Goods goods=goodsMapper.getGoodsById(id);

        String attrsText=joinAttrsText(skuId);

        int postFee=0;
        String price= String.valueOf(cartMapper.getPriceBySkuId(skuId));
        Integer price_int=Integer.parseInt(price.split("\\.")[0]);
        String totalPrice= String.valueOf(price_int*count);
        String totalPayPrice= String.valueOf(Integer.parseInt(totalPrice)+postFee);

        goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(goods.getId()));
        CartItem cartItem=new CartItem(goods.getId(),price,count,skuId,goods.getName(),
                attrsText,new ArrayList<>(),goods.getMainPictures().get(0),price,price,true,
                goods.getInventory(),false,false,postFee,price,totalPrice,totalPayPrice,-1);

        int user_id=userService.getUserIdIfLogin();

        Integer SkuIdCount=cartMapper.getSkuIdCount(skuId,user_id);
        if(SkuIdCount==0){
            cartMapper.addCartList(Double.valueOf(cartItem.getPrice()),count,goods.getId(),skuId,user_id);
        }
        else{
            cartMapper.updateCountBySkuId(skuId,SkuIdCount+count);
        }
        return cartItem;
    }

    public void CompleteCartItemList(List<CartItem> cartItemList){
        for(CartItem item:cartItemList){
            Goods goods=goodsMapper.getGoodsById(item.getId());
            item.setName(goods.getName());

            String attrsText=joinAttrsText(item.getSkuId());
            item.setAttrsText(attrsText);

            goods.setMainPictures(goodsMapper.getMainPicturesByGoodsId(goods.getId()));
            item.setPicture(goods.getMainPictures().get(0));

            item.setNowPrice(item.getPrice());
            item.setNowOriginalPrice(item.getPrice());

            item.setSelected(false);
            item.setStock(goods.getInventory());

            item.setEffective(false);

            item.setCollect(false);

            item.setPostFee(0);
        }
    }

    @Override
    public List<CartItem> getCartList() {
        int user_id= userService.getUserIdIfLogin();
        List<CartItem> cartItemList=cartMapper.getCartListByUserId(user_id);

        CompleteCartItemList(cartItemList);

        return cartItemList;
    }

    @Override
    public List<CartItem> getCartList(List<Integer> skus){
        int user_id=userService.getUserIdIfLogin();
        List<CartItem> cartItemList=new ArrayList<>();
        for(Integer sku:skus){
            cartItemList.add(cartMapper.getCartListByUserIdAndSkuId(user_id,sku));
        }

        CompleteCartItemList(cartItemList);

        return cartItemList;
    }

    @Override
    public void mergeCartList(List<Map<String, Object>> cartList) {
        Map<String, Object> u=ThreadLocalUtil.get();
        Integer user_id= Integer.parseInt((String) u.get("id"));
        for(Map<String,Object> cart:cartList){
            Integer count= (Integer) cart.get("count");
            Integer skuId=(Integer) cart.get("skuId");
            Integer SkuIdCount=cartMapper.getSkuIdCount(skuId,user_id);
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
    public boolean deleteCartList(List<Integer> list){
        Map<String, Object> u=ThreadLocalUtil.get();
        Integer user_id=Integer.parseInt((String) u.get("id")) ;
        try{
            for(Integer id:list){
                cartMapper.deleteBySkuIdAndUserId(id,user_id);
            }
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
}
