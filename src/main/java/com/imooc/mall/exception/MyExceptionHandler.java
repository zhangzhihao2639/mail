package com.imooc.mall.exception;

import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.UnexpectedTypeException;
import java.util.HashMap;
import java.util.Map;

import static com.imooc.mall.enums.ResponseEnum.ERROR;
import static com.imooc.mall.enums.ResponseEnum.NEED_LOGIN;
import static com.imooc.mall.enums.ResponseEnum.PARAM_ERROR;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Map responseVo(Exception e){

        Map map =new HashMap();
        map.put("code","500");
        map.put("msg",e.getMessage());
        return  map;
    }
    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseVo exception(RuntimeException e){
       return ResponseVo.error(ERROR,e.getMessage());
    }
/*
    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseVo OrderException(UnexpectedTypeException e){

        return  ResponseVo.error();
    }
*/

    @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginException(RuntimeException e){
        return ResponseVo.error(NEED_LOGIN);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVo RequestParameterException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();
        return  ResponseVo.error(ResponseEnum.PARAM_ERROR,bindingResult.getFieldError().getField()+bindingResult.getFieldError().getDefaultMessage());
    }
}
