package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.entity.Product;
import com.imooc.mall.service.IPoductService;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private IPoductService poductService;
    @GetMapping("/products")
    public ResponseVo<PageInfo> product(@RequestParam(required = false) Integer categoryId,
                                         @RequestParam(required = false ,defaultValue = "1") Integer pageNum,
                                         @RequestParam(required = false ,defaultValue = "10") Integer pageSize){
    return poductService.list(categoryId,pageNum,pageSize);

    }
    @GetMapping("/product/{productId}")
    public ResponseVo<ProductDetailVo>productDetailVo(@PathVariable Integer productId){
        return  poductService.detail(productId);
    }
}
