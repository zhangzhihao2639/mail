package com.imooc.mall.service;

import com.imooc.mall.entity.User;
import com.imooc.mall.vo.ResponseVo;

public interface IUserService {
    /**
     * 注册账号
     * @param user
     */
    ResponseVo<User> Register(User user);

    /**
     * 登陆
     * @param username
     * @return
     */
    ResponseVo<User>login(String username, String password);
}
