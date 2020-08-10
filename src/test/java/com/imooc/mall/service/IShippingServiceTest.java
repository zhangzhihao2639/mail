package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.entity.Shipping;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Categories;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;
@Slf4j
public class IShippingServiceTest extends MallApplicationTests {
    @Autowired
    private  IShippingService shippingService;
    private Gson gson= new GsonBuilder().setPrettyPrinting().create();
    private  Integer uid = 1;
    @Test
    @Ignore
    public void add() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverAddress("北京文化园");
        shippingForm.setReceiverCity("北京");
        shippingForm.setReceiverDistrict("朝阳区");
        shippingForm.setReceiverMobile("1556653352");
        shippingForm.setReceiverName("龙七");
        shippingForm.setReceiverProvince("北京市");
        shippingForm.setReceiverZip("000000");
        shippingForm.setReceiverPhone("15345435131");
        ResponseVo<Map<String,Integer>>responseVo = shippingService.add(uid,shippingForm);
        log.info("result:{}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    @Ignore
    public void delete() {
      ResponseVo responseVo = shippingService.delete(uid,2);
      log.info("result:{}",gson.toJson(responseVo));
      Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    @Ignore
    public void update() {
        ShippingForm shippingForm = new ShippingForm();
        shippingForm.setReceiverName("李四");
        shippingForm.setReceiverProvince("四川");
        shippingForm.setReceiverCity("成都");
        ResponseVo responseVo= shippingService.update(uid,3,shippingForm);
        log.info("result:{}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test

    public void list() {
        ResponseVo<PageInfo> list = shippingService.list(uid, 1, 3);
        log.info("result:{}",gson.toJson(list));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),list.getStatus());
    }
}