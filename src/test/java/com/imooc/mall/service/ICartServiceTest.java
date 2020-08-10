package com.imooc.mall.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;
@Slf4j
public class ICartServiceTest extends MallApplicationTests {
    @Autowired
    private ICartService cartService;
    private Gson gson= new GsonBuilder().setPrettyPrinting().create();
    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(29);
        form.setSelected(true);
       ResponseVo<CartVo>responseVo =  cartService.add(1,form);
       log.info("添加购物车：{}",gson.toJson(responseVo));
    }
    @Test
    public void list(){
        ResponseVo<CartVo>list = cartService.list(1);
        log.info("list:{}",gson.toJson(list));
    }
    @Test
    public void update(){
        CartUpdateForm form = new CartUpdateForm();
        form.setSelect(false);
        form.setQuantity(10);
        ResponseVo<CartVo>responseVo = cartService.update(1,28,form);
        log.info("update:{}",gson.toJson(responseVo));
    }
    @Test
    public void delete(){
        ResponseVo<CartVo>responseVo = cartService.delete(1,28);
        log.info("delete:{}",gson.toJson(responseVo));
    }

    @Test
    public void sellectAll(){
        ResponseVo<CartVo>responseVo = cartService.sellectAll(1);
        log.info("sellectAll:{}",gson.toJson(responseVo));
    }
    @Test
    public void unSellectAll(){
        ResponseVo<CartVo>responseVo = cartService.unSellectAll(1);
        log.info("unSellectAll:{}",gson.toJson(responseVo));
    }
    @Test
    public void sum(){
        ResponseVo<Integer>responseVo = cartService.sum(1);
        log.info("sum:{}",gson.toJson(responseVo));
    }
}