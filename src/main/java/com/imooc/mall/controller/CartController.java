package com.imooc.mall.controller;

import com.imooc.mall.entity.Cart;
import com.imooc.mall.entity.User;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConsta.CURRENT_USER;


@RestController
public class CartController {
    @Autowired
    private ICartService cartService;
    @GetMapping("/carts/list")
    public ResponseVo<CartVo>list(HttpSession session){
    User user = (User) session.getAttribute(CURRENT_USER);
    return cartService.list(user.getId());
    }
    @PostMapping("/carts/add")
    public ResponseVo<CartVo> add(@RequestBody @Valid CartAddForm cartAddForm, HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);
    }
    @PostMapping("/carts/update/{productId}")
    public ResponseVo<CartVo>update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm cartUpdateForm,
                                     HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return  cartService.update(user.getId(),productId,cartUpdateForm);
    }
    @GetMapping("carts/delete")
    public ResponseVo<CartVo>delete(@RequestParam Integer productId,HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.delete(user.getId(), productId);
    }
    @PutMapping("carts/sellectAll")
    public ResponseVo<CartVo>sellectAll(HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.sellectAll(user.getId());
    }
    @PutMapping("carts/unSellectAll")
    public ResponseVo<CartVo>unSellectAll(HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.unSellectAll(user.getId());
    }
    @GetMapping("carts/sum")
    public ResponseVo<Integer>sum(HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
