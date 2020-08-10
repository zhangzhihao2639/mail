package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.entity.Product;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPoductService {
    ResponseVo<PageInfo>list(Integer id, Integer pagNum, Integer pageNum);

    ResponseVo<ProductDetailVo>detail(Integer productId);
}
