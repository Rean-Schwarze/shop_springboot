package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.FileMapper;
import com.rean.shopspring.service.FileService;
import com.rean.shopspring.utils.AliOssUtil;
import com.rean.shopspring.utils.ThreadLocalUtil;
import org.apache.ibatis.session.SqlSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.Map;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;

    // 上传商品图片
    @Override
    @Transactional
    public boolean uploadGoodsPictures(String name, InputStream in, Integer goods_id, String type){
        Map<String,Object> sellerMap= ThreadLocalUtil.get();
        Integer seller_id= (Integer) sellerMap.get("id");
        String ac_id=fileMapper.getACCESSKEYIDbyRamName((String) sellerMap.get("type"));
        String ac_sec=fileMapper.getACCESSKEYSECRETbyRamName((String) sellerMap.get("type"));
        StringBuilder tmp=new StringBuilder();
        try{
            String url = AliOssUtil.uploadFile(name,in,ac_id,ac_sec);
            tmp= new StringBuilder(url.split(".com/")[1]);
            switch(type){
                case "main" ->{
                    fileMapper.addGoodsMainPictures(url,goods_id,seller_id);
                }
                case "detail" ->{
                    fileMapper.addGoodsDetailPictures(url,goods_id,seller_id);
                }
            }
            return true;
        }
        catch (Exception e){
            if(!tmp.isEmpty()){
                AliOssUtil.deleteFile(String.valueOf(tmp),ac_id,ac_sec);
            }
            return false;
        }
    }
}
