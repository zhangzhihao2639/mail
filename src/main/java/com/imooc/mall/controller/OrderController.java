package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.entity.User;
import com.imooc.mall.form.OrderCreateForm;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConsta.CURRENT_USER;

@RestController
public class OrderController {
    @Autowired
    private IOrderService orderService;
    @PostMapping("/orders")
    public ResponseVo<OrderVo> create(@Valid @RequestBody OrderCreateForm orderCreateForm
                                        ,HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
       return orderService.create(user.getId(),orderCreateForm.getShippingId());
    }
    @GetMapping("/orders/list")
    public ResponseVo<PageInfo> list(@RequestParam(required = false ,defaultValue = "10") Integer pageSize,
                                     @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                                     HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return orderService.list(user.getId(),pageNum,pageSize);
    }
    @GetMapping("/orders/detail/{orderNo}")
    public  ResponseVo<OrderVo>detail(@PathVariable Long orderNo,
                                      HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return  orderService.detail(user.getId(),orderNo);
    }
    @PutMapping("/orders/concel/{orderNo}")
    public ResponseVo concel(@PathVariable Long orderNo,
                             HttpSession session){
        User user = (User) session.getAttribute(CURRENT_USER);
        return orderService.concel(user.getId(),orderNo);
    }

}
