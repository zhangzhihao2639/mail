package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum  ProductStasusEnum {
    ON_SELE(1),
    OFF_SELL(2),
    DELETE_PRODUCT(3)
    ;
    Integer code;
    ProductStasusEnum (Integer code){
        this.code = code;
    }
}
