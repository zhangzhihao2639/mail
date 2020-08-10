package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum  ResponseEnum {
    ERROR(-1,"服务段错误"),
    SUCCESS(0,"成功"),
    PASSWORD(1,"密码错误"),
    USERNAME_EXIST(2,"用户存在"),
    PARAM_ERROR(3,"參數錯誤"),
    EMAIL_EXIST(4,"用户存在"),
    OFF_SELL_0R_DELETE_PRODUCT(5,"商品下架或已删除"),
    PRODUCT_NOT_EXIST(6,"商品不存在"),
    PRODUCT_STOCK_ERROR(7,"库存不正确"),
    CART_SUCCESS(9,"购物车添加商品成功"),
    NEED_LOGIN(10,"用户未登陆"),
    USERNAME_OR_PASSWORD(11,"用户名或密码错误"),
    ADD_ADDRESS_SUCCESS(12,"添加地址失败"),
    DELETE_ADDRESS_ERROR(13,"删除地址失败"),
    UPDATE_ADDRESS_ERROR(14,"更新地址失败"),
    ADDRESS_NOT_EXIST(15,"地址不存在"),
    CART_NOT_SELECT(16,"请选中商品进行下单"),
    ORDER_NOT_EXIST(17,"订单不存在"),
    ORDER_STATUS_ERROR(18,"订单状态错误")
    ;
    Integer code;
    String desc;
    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc =desc;
    }
}

