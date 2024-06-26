package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.CategoryMapper;
import com.rean.shopspring.mapper.GoodsMapper;
import com.rean.shopspring.mapper.OrderMapper;
import com.rean.shopspring.mapper.SellerMapper;
import com.rean.shopspring.pojo.*;
import com.rean.shopspring.service.SellerService;
import com.rean.shopspring.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import static com.rean.shopspring.utils.GoodsUtil.getAttrsText;


@Service
public class SellerServiceImpl implements SellerService {
    @Autowired
    private SellerMapper sellerMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public Seller findByName(String name){
        return sellerMapper.findByName(name);
    }

    @Override
    public Seller findById(Integer id) { return sellerMapper.findById(id); }

    // 获取负责的分类
    @Override
    public List<SellerCategory> getSellCategory(int seller_id){
        List<SellerCategory> categoryList=new ArrayList<>();
        Set<Integer> categoryIdSet=new HashSet<>();
        Map<Integer,List<Integer>> categoryIdMap=new HashMap<>();
        Map<Integer,Boolean> categoryIdAndIsAllSubMap=new HashMap<>();

        List<Seller_categories> seller_categories=sellerMapper.getSellCategory(seller_id);

//        先用Map获取负责的父、子分类id
        for(Seller_categories seller_category : seller_categories){
            int category_id=seller_category.getCategoryId();
            categoryIdSet.add(category_id);

            categoryIdAndIsAllSubMap.put(category_id,seller_category.isAllSub());

            if(seller_category.isAllSub()){
                categoryIdMap.put(category_id,categoryMapper.getSubCategoryIdByParentId(category_id));
            }
            else{
                if(categoryIdMap.get(category_id)!=null){
                    categoryIdMap.get(category_id).add(seller_category.getSubCategoryId());
                }
                else{
                    List<Integer> tmp=new ArrayList<>();
                    tmp.add(seller_category.getSubCategoryId());
                    categoryIdMap.put(category_id,tmp);
                }
            }
        }

//        遍历父分类，添加子分类
        for(Integer category_id:categoryIdSet){
            SellerCategory category=categoryMapper.getSellerCategoryById(category_id);
            category.setAllSub(categoryIdAndIsAllSubMap.get(category_id));
            List<Category> children=new ArrayList<>();
            for(Integer sub_id:categoryIdMap.get(category_id)){
                children.add(categoryMapper.getSubCategoryById(sub_id));
            }
            category.setChildren(children);
            categoryList.add(category);
        }

        return categoryList;
    }

    // 获取负责分类下的商品id列表
    public List<Integer> getSellGoodsId(int seller_id, int category_id, String type){
        // 首先获取品牌id
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);
        if(Objects.equals(type, "main")){
            return goodsMapper.getGoodsIdByBrandAndCategory(brand_id,category_id);
        }
        else if(Objects.equals(type,"sub")){
            return goodsMapper.getGoodsIdByBrandAndSubCategory(brand_id,category_id);
        }
        else{ // 获取所有负责分类下的商品
            List<SellerCategory> categoryList=getSellCategory(seller_id);
            Set<Integer> goodsSet=new HashSet<>();
            for(SellerCategory sellerCategory:categoryList){
                if(sellerCategory.isAllSub()){
                    List<Integer> tmp=goodsMapper.getGoodsIdByBrandAndCategory(brand_id,sellerCategory.getId());
                    goodsSet.addAll(tmp);
                }
                else{
                    for(Category sub:sellerCategory.getChildren()){
                        List<Integer> tmp=goodsMapper.getGoodsIdByBrandAndSubCategory(brand_id,sub.getId());
                        goodsSet.addAll(tmp);
                    }
                }
            }
            return new ArrayList<>(goodsSet);
        }
    }

    // 添加商品
    public Integer addGoods(int seller_id, SellerGoodsRequest request){
        // 首先获取品牌id
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);

        request.setAddSeller(seller_id);
        request.setBrandId(brand_id);

        goodsMapper.addGoods(request);

        int id=request.getId();

        // 添加规格
        Map<String,Map<String,Integer>> specs_map=new HashMap<>();
        for(Specs specs:request.getSpecs()){
            specs.setGoodsId(id);
            goodsMapper.addSpecs(specs);
            int specs_id=specs.getId();
            // 添加具体规格
            Map<String,Integer> specs_values_map=new HashMap<>();
            for(Spec_values spec_values:specs.getValues()){
                spec_values.setGoodsId(id);
                spec_values.setSpecsId(specs_id);
                goodsMapper.addSpecsValues(spec_values);
                int value_id=spec_values.getId();
                specs_values_map.put(spec_values.getName(),value_id);
            }
            specs_map.put(specs.getName(),specs_values_map);
        }

        // 添加sku
        int minPrice=Integer.MAX_VALUE;
        for(Sku sku: request.getSkus()){
            // 计算最低价格
            minPrice=Integer.min(Integer.parseInt(sku.getPrice()),minPrice);
            List<Spec_sku> spec_skus=sku.getSpecs();
            List<Integer> values=new ArrayList<>();
            for(Spec_sku spec_sku:spec_skus){
                values.add(specs_map.get(spec_sku.getName()).get(spec_sku.getValueName()));
            }
            sku.setGoodsId(id);
            if(values.size()==1){
                sku.setSpecsValuesId(values.get(0));
            }
            else{
                sku.setSpecsValuesId(values.get(0));
                sku.setSpecsValuesId2(values.get(1));
            }
            goodsMapper.addSku(sku);
        }

        // 修改goods价格
        goodsMapper.updateGoodsPrice(String.valueOf(minPrice),String.valueOf(minPrice),id);

        return id;
    }

    // 删除商品（伪
    public void deleteGoodsFake(Integer id, Integer seller_id){
        goodsMapper.deleteGoodsFake(id);
    }

    // 修改商品价格、库存
    public void updateGoodsPriceAndInventory(Integer id, String price, Integer inventory, Integer goods_id, Integer seller_id){
        // 修改sku表内价格
        String oldPrice=goodsMapper.getPriceBySkuId(id);
        goodsMapper.updateSkuPrice(id,price,oldPrice);
        goodsMapper.updateSkuInventory(id,inventory);

        // 修改goods表内价格
        List<Sku> skus=goodsMapper.getSkuByGoodsId(goods_id);
        Integer oldPriceGoods=goodsMapper.getPriceByGoodsId(goods_id);
        int tmp=Integer.MAX_VALUE;
        for(Sku sku:skus){
            tmp=Integer.min(tmp, Integer.parseInt(sku.getPrice()));
        }
        goodsMapper.updateGoodsPrice(String.valueOf(tmp),String.valueOf(oldPriceGoods),goods_id);
    }

    public Integer getOrderItemCounts(Integer seller_id, Integer orderState){
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);
        if(orderState==0){
            return orderMapper.getOrderItemCounts(brand_id);
        }
        else{
            return orderMapper.getOrderItemCountsByState(brand_id,orderState);
        }
    }

    public Integer getOrderItemCounts(Integer goods_id){
        return orderMapper.getOrderItemCountByGoodsId(goods_id);
    }

    // 获取负责商品相关订单项目
    public SellerOrderResponse getOrderLists(Integer seller_id, Integer orderState, Integer start, Integer pageSize){
        Integer brand_id=sellerMapper.getSellBrandId(seller_id);
        Integer total=getOrderItemCounts(seller_id,orderState);
        List<OrderItem> orderItemList;
        if(orderState==0){
            orderItemList=orderMapper.getOrderItemByRange(brand_id,start,pageSize);
        }
        else{
            orderItemList=orderMapper.getOrderItemByRangeAndState(brand_id,start,pageSize,orderState);
        }


        SellerOrderResponse response=new SellerOrderResponse();
        response.setTotal(total);
        List<SellerOrderResponse.Items> itemsList = new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            Integer goods_id=goodsMapper.getGoodsIdBySkuId(orderItem.getSkuId());
            SellerOrderResponse.Skus skus=new SellerOrderResponse.Skus(orderItem.getSkuId(),
                    goodsMapper.getMainPicturesByGoodsId(goods_id).get(0), goodsMapper.getNameByGoodsId(goods_id),
                    getAttrsText(orderItem.getSkuId(),goodsMapper),orderItem.getCount(),orderItem.getPayMoney());
            Order order=orderMapper.getOrderById(orderItem.getOrderId());
            // 更新付款倒计时
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
            SellerOrderResponse.Items items=new SellerOrderResponse.Items(orderItem.getId(),orderItem.getOrderId(),
                    orderState,orderItem.getCreateTime(),order.getCountdown(),skus,orderItem.getUserId());
            itemsList.add(items);
        }
        response.setItems(itemsList);
        return response;
    }

    // 修改密码
    public void updatePassword(Integer id,String newPassword){
        String md5Password= Md5Util.getMD5String(newPassword);
        sellerMapper.updatePassword(id,md5Password);
    }
}
