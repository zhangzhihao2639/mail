package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.entity.User;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

import static com.imooc.mall.enums.ResponseEnum.*;

@Service
@Slf4j
public class UserServiceImpl implements IUserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     *
     * @param user
     */
    @Override
    public ResponseVo Register(User user) {
        //判断数据库用户名和邮箱是否重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            return ResponseVo.error(USERNAME_EXIST);
        }
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            return ResponseVo.error(EMAIL_EXIST);
        }
        //Md5加密，摘要
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8)));
        //角色注冊等級
        user.setRole(1);
        //写入数据库
        int insert = userMapper.insertSelective(user);
        if (insert < 0) {
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success();
    }

   @Override
    public ResponseVo<User> login(String username,String password) {
        User user = userMapper.selectByUsername(username);
        log.info("user:{}",user);
      if(user== null){
            //用户名不存在，返回用户名或密码错误
            return ResponseVo.error(USERNAME_OR_PASSWORD);
        }
        if(!DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8)).equals(user.getPassword())){
            //密码错误，返回用户名或密码错误
            return ResponseVo.error(USERNAME_OR_PASSWORD);
        }
        user.setPassword("");
        return ResponseVo.success(user);
    }
    }




