package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.FileService;
import com.rean.shopspring.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
public class FileUploadController {
    @Autowired
    private FileService fileService;
    @PostMapping("/upload")
    public Result upload(@RequestParam("files") MultipartFile[] files) throws Exception {
        //把文件的内容存储到本地磁盘上
        for(MultipartFile file:files){
            String originalFilename = file.getOriginalFilename();
            String[] nameList= new String[0];
            if (originalFilename != null) {
                nameList = originalFilename.split("\\.")[0].split("_");
            }
            String name=nameList[0];
            String type=nameList[1];
            //保证文件的名字是唯一的,从而防止文件覆盖
            String filename = "images/"+UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
            String url = AliOssUtil.uploadFile(filename,file.getInputStream());

            switch (type) {
                case "main" -> {
                    int id = fileService.findGoodsIdByName(name);
                    fileService.addMainPictures(url, id);
                }
                case "detail" -> {
                    int id = fileService.findGoodsIdByName(name);
                    fileService.addDetailPictures(url, id);
                }
                case "category" -> {
                    int id = fileService.findCategoryIdByName(name);
                    fileService.addCategoryPictures(url, id);
                }
                case "sub" -> {
                    int id = fileService.findSubCategoryIdByName(name);
                    fileService.addSubCategoryPictures(url, id);
                }
                case "banner" -> {
                    fileService.addBannerPictures(url);
                }
            }
        }
        return Result.success();
    }
}
