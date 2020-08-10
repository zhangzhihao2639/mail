package com.imooc.mall.service.impl;


import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.entity.User;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.enums.RoleEnum;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class UserServiceImplTest extends MallApplicationTests {
    public static final String USERNAME="张三111";
    public static final String PASSWORD="112";
    @Autowired
    private IUserService userService;
    @Test
    public void register() {
        User user = new User(USERNAME,PASSWORD,"1234561@qq.com", RoleEnum.CUSTOMER.getCode());
        userService.Register(user);
    }
    @Test
    public void login(){
        register();
        ResponseVo<User> responseVo = userService.login(USERNAME,PASSWORD);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}