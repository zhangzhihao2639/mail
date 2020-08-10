package com.imooc.mall.interceptor;

import com.imooc.mall.entity.User;
import com.imooc.mall.exception.UserLoginException;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.mall.consts.MallConsta.CURRENT_USER;

@Slf4j
public class UserLoginInceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
        //falst表示中断流程，true表示继续流程
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        if(user == null){
            log.info("user == null");
            throw new UserLoginException();
        }
        return true;
    }
}
