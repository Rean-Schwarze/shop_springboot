package com.rean.shopspring.service.impl;

import com.rean.shopspring.mapper.FileMapper;
import com.rean.shopspring.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileServiceImpl implements FileService {
    @Autowired
    private FileMapper fileMapper;
    @Override
    public int findGoodsIdByName(String name){
        return fileMapper.findGoodsIdByName(name);
    }
    @Override
    public int findCategoryIdByName(String name){
        return fileMapper.findCategoryIdByName(name);
    }
    @Override
    public int findSubCategoryIdByName(String name){
        return fileMapper.findSubCategoryIdByName(name);
    }
    @Override
    public void addMainPictures(String url,int id){
        fileMapper.addMainPictures(url,id);
    }

    @Override
    public void addDetailPictures(String url,int id){
        fileMapper.addDetailPictures(url,id);
    }

    @Override
    public void addCategoryPictures(String url,int id){
        fileMapper.addCategoryPictures(url,id);
    }

    @Override
    public void addSubCategoryPictures(String url,int id){
        fileMapper.addSubCategoryPictures(url,id);
    }

    @Override
    public void addBannerPictures(String url) {fileMapper.addBannerPictures(url);}
}
