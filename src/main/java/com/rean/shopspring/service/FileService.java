package com.rean.shopspring.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    int findGoodsIdByName(String name);

    int findCategoryIdByName(String name);

    int findSubCategoryIdByName(String name);

    void addMainPictures(String url, int id);

    void addDetailPictures(String url, int id);

    void addCategoryPictures(String url, int id);

    void addSubCategoryPictures(String url, int id);

    void addBannerPictures(String url);
}
