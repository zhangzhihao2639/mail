package com.imooc.mall.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.entity.Shipping;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.vo.ResponseVo;

import java.util.Map;

public interface IShippingService {
    /**
     * 添加地址
     * @param shippingForm
     * @return
     */
    ResponseVo<Map<String,Integer>>add(Integer uid, ShippingForm shippingForm);

    /**
     * 删除地址
     * @param shippingId
     * @return
     */
    ResponseVo delete(Integer uid, Integer shippingId);

    /**
     * 更新地址
     * @param uid
     * @param shippingId
     * @param shippingForm
     * @return
     */
    ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm);

    /**
     * 地址列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo>list(Integer uid, Integer pageNum, Integer pageSize);
}
