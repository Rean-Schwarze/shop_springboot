package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.FileService;
import com.rean.shopspring.service.UserService;
import com.rean.shopspring.utils.AliOssUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/upload")
public class FileUploadController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    // 上传商品图片
    @PostMapping("/seller/goods")
    public Result uploadGoodsImages(@RequestParam("files") MultipartFile[] files,
                                    @RequestParam("goodsId") Integer goods_id,
                                    @RequestParam("type") String type) throws Exception {
        //把文件的内容存储到本地磁盘上
        for(MultipartFile file:files){
            String originalFilename = file.getOriginalFilename();
            //保证文件的名字是唯一的,从而防止文件覆盖
            String filename = null;
            if (originalFilename != null) {
                filename = "images/"+ UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            boolean tmp=fileService.uploadGoodsPictures(filename,file.getInputStream(),goods_id,type);
            if(!tmp){
                return Result.error("上传【"+originalFilename+"】时失败！");
            }
        }
        return Result.success();
    }

    @PostMapping("/avatar")
    public Result<Map<String,String>> uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        String originalFilename = file.getOriginalFilename();
        String uploadFilename = "";
        if (originalFilename != null) {
            String filename=UUID.randomUUID().toString()+originalFilename.substring(originalFilename.lastIndexOf("."));
            uploadFilename="user/avatar/"+filename;

            String url=userService.uploadUserAvatar(uploadFilename,file.getInputStream());

            Map<String,String> result=new HashMap<>();
            result.put("name",filename);
            result.put("url",url);
            return Result.success(result);
        }
        else{
            return Result.error("文件名为空！");
        }
    }
}
