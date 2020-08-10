package com.imooc.mall.controller;

import com.github.pagehelper.PageInfo;
import com.imooc.mall.consts.MallConsta;
import com.imooc.mall.entity.User;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.service.IShippingService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Map;

@RestController
public class ShippingController {
    @Autowired
    private IShippingService shippingService;
    @PostMapping("/shippings")
    public ResponseVo<Map<String,Integer>>add(@Valid @RequestBody ShippingForm shippingForm, HttpSession session){
        User user = (User) session.getAttribute(MallConsta.CURRENT_USER);
        return shippingService.add(user.getId(), shippingForm);
    }
    @DeleteMapping("/shippings/{shippingId}")
    public ResponseVo delete(@PathVariable("shippingId") Integer shippingId,
                              HttpSession session){
        User user = (User) session.getAttribute(MallConsta.CURRENT_USER);
       return shippingService.delete(user.getId(),shippingId);
    }
    @PutMapping("/shippings/{shippingId}")
    public ResponseVo update(@PathVariable("shippingId") Integer shippingId,
                             @Valid @RequestBody ShippingForm shippingForm,
                             HttpSession session){
        User user = (User) session.getAttribute(MallConsta.CURRENT_USER);
        return shippingService.update(user.getId(),shippingId,shippingForm);
    }
    @GetMapping("/shippings**")
    public ResponseVo<PageInfo>list(@RequestParam(required = false,defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false,defaultValue = "10")Integer pageSize,
                                    HttpSession session){
        User user = (User) session.getAttribute(MallConsta.CURRENT_USER);
        return shippingService.list(user.getId(),pageNum,pageSize);
    }
}
