package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

public class IPoductServiceTest extends MallApplicationTests {
    @Autowired
    private IPoductService poductService;
    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = poductService.list(null, 1, 10);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }
    @Test
    public void  detail(){
        ResponseVo<ProductDetailVo> responseVo = poductService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}