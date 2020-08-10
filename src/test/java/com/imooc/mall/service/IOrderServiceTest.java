package com.imooc.mall.service;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@Slf4j
@Transactional
public class IOrderServiceTest extends MallApplicationTests {
    private Integer uid = 4;
    private Integer shippingId = 7;
    private Integer productId = 29;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICartService cartService;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Before
    public void add(){
        CartAddForm form = new CartAddForm();
        form.setProductId(productId);
        form.setSelected(true);
        ResponseVo<CartVo>responseVo =  cartService.add(uid,form);
        log.info("添加购物车：{}",gson.toJson(responseVo));
    }
    @Test
    public void creatTest(){
        ResponseVo<OrderVo> responseVo = create();
    }
    private ResponseVo<OrderVo> create() {
        ResponseVo<OrderVo> orderVoResponseVo = orderService.create(uid, shippingId);
        log.info("resultCreate:{}",gson.toJson(orderVoResponseVo));
        return orderVoResponseVo;
    }
    @Test
    public void list(){
        ResponseVo<PageInfo> responseVo = orderService.list(uid,1,10);
        log.info("resultList:{}",gson.toJson(responseVo));
    }

    @Test
    public void detail(){
        ResponseVo<OrderVo> vo =create();
        ResponseVo<OrderVo> responseVo = orderService.detail(uid,vo.getData().getOrderNo());
        log.info("resultDetail:{}",gson.toJson(responseVo));
    }

    @Test
    public void concel(){
        ResponseVo<OrderVo> vo =create();
        ResponseVo<OrderVo> responseVo = orderService.concel(uid,vo.getData().getOrderNo());
        log.info("resultConcel:{}",gson.toJson(responseVo));
    }
}