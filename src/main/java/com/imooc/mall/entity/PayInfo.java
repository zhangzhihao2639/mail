package com.imooc.mall.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayInfo {
    private Integer id;

    private Long userId;

    private Long orderNo;//订单号

    private Integer payPlatform;//支付平台

    private String platformNumber;//支付流水号

    private String platformStatus;//支付状态

    private BigDecimal payAmount;//支付金额

    private Date createTime;

    private Date updateTime;

    public PayInfo(Long orderNo, Integer payPlatform, String platformStatus, BigDecimal payAmount) {
        this.orderNo = orderNo;
        this.payPlatform = payPlatform;
        this.platformStatus = platformStatus;
        this.payAmount = payAmount;
    }
}