package com.imooc.mall.controller;


import com.imooc.mall.entity.User;
import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.service.IUserService;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConsta.CURRENT_USER;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;


@RestController
@Slf4j
public class UserController {
    @Autowired
    private IUserService iUserservice;
    @PostMapping("/user/register")
    public ResponseVo<User> register(@RequestBody @Valid UserRegisterForm uerRegisterForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            log.error("请求提交栋参数有误,{},{}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }
        User user = new User();
        BeanUtils.copyProperties(uerRegisterForm,user);
        return iUserservice.Register(user);
    }
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm,
                                  BindingResult bindingResult,
                                  HttpSession session){
        if(bindingResult.hasErrors()){
            log.error("请求提交栋参数有误,{},{}",
                    bindingResult.getFieldError().getField(),
                    bindingResult.getFieldError().getDefaultMessage());
            return ResponseVo.error(PARAM_ERROR, bindingResult);
        }
        //设置session值
        ResponseVo<User> userResponseVo =  iUserservice.login(userLoginForm.getUsername(),userLoginForm.getPassword());
        session.setAttribute(CURRENT_USER,userResponseVo.getData());
        log.info("login session={}",session.getId());
        return userResponseVo;
    }
    @GetMapping("/user")
    //session保存在内存里，改进版 token+redis
    public ResponseVo<User> info(HttpSession session){
        log.info("user session={}",session.getId());
        User user = (User) session.getAttribute(CURRENT_USER);
        return ResponseVo.success(user);
    }
    //登出
    @PostMapping("/user/loginOut")
    public ResponseVo loginOut(HttpSession session){
        log.info("loginOut session={}",session);
        User user = (User) session.getAttribute(CURRENT_USER);
        session.removeAttribute(CURRENT_USER);
        return ResponseVo.success();
    }
}
