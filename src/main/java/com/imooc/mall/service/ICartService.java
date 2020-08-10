package com.imooc.mall.service;

import com.imooc.mall.entity.Cart;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;

public interface ICartService {
    ResponseVo<CartVo>add(Integer uid, CartAddForm cartAddForm);
    ResponseVo<CartVo>list(Integer uid);
    ResponseVo<CartVo>update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm);
    ResponseVo<CartVo>delete(Integer uid, Integer productId);
    ResponseVo<CartVo>sellectAll(Integer uid);
    ResponseVo<CartVo>unSellectAll(Integer uid);
    ResponseVo<Integer>sum(Integer uid);
    List<Cart>listForCart(Integer uid);
}