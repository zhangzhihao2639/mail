package com.imooc.mall.enums;

import lombok.Getter;

@Getter
public enum OrderStatusEnum {
        CANCELED(0,"已取消"),

        NO_PAY(1,"未支付"),

        PAID(2,"已支付"),

        SHIPPED(3,"已发货"),

        TRADE_SUCCESS(4,"交易成功"),

        TRADE_CLOSE(5,"交易关闭")
        ;
        Integer code;

        String desc;

        OrderStatusEnum(Integer code,String desc){
         this.code = code;
         this.desc = desc;
    }

}
