package com.imooc.mall.listener;

import com.google.gson.Gson;
import com.imooc.mall.entity.PayInfo;
import com.imooc.mall.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "PayNotify")
@Slf4j
public class PayMsgListener {
    @Autowired
    private IOrderService orderService;
   @RabbitHandler
   public void  proess(String msg){
       log.info("接受到消息{}",msg);
       PayInfo payInfo = new Gson().fromJson(msg, PayInfo.class);
       if(payInfo.getPlatformStatus().equals("success")){
           //修改订单状态
           orderService.payId(payInfo.getOrderNo());
       }
   }
}
