package com.imooc.mall.form;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


@Data
public class UserLoginForm {
    //notblank 用于string判断空格
    //notempty 用于判断集合
    //notnull
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
