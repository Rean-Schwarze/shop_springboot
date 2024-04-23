package com.rean.shopspring.controller;

import com.rean.shopspring.pojo.Result;
import com.rean.shopspring.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/sub/filter")
    @ResponseBody
    public Result<Map<String,Object>> getCategoryFilter(@RequestParam("id") String id){
        return Result.success(categoryService.getCategoryFilter(Integer.parseInt(id)));
    }

    @RequestMapping("/goods/temporary")
    @ResponseBody
    public Result<Map<String,Object>> getSubCategory(@RequestBody HashMap<String, String> map){
        String categoryId= map.get("categoryId");
        String page= map.get("page");
        String pageSize= map.get("pageSize");
        String sortField= map.get("sortField");
        return Result.success(categoryService.getSubCategoryAndGoods(categoryId,page,pageSize,sortField));
    }
}
