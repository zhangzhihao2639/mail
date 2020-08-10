package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.entity.Shipping;
import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.service.IShippingService;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.imooc.mall.enums.ResponseEnum.*;

@Service
public class ShippingServiceImpl implements IShippingService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ResponseVo<Map<String,Integer>> add(Integer uid, ShippingForm shippingForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm,shipping);
        shipping.setUserId(uid);
        Integer resoult = shippingMapper.insertSelective(shipping);
        if(resoult == 0){
            return ResponseVo.error(ADD_ADDRESS_SUCCESS);
        }
        Map<String, Integer>map = new HashMap<>();
        map.put("shippingId",shipping.getId());
        return ResponseVo.success(map);
    }

    @Override
    public ResponseVo delete(Integer uid, Integer shippingId) {
        Integer result = shippingMapper.deleteByIdAndUid(uid,shippingId);
        if(result == 0){
            return ResponseVo.error(DELETE_ADDRESS_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo update(Integer uid, Integer shippingId, ShippingForm shippingForm) {
        Shipping shipping = new Shipping();
        BeanUtils.copyProperties(shippingForm,shipping);
        shipping.setUserId(uid);
        shipping.setId(shippingId);
        int result = shippingMapper.updateByPrimaryKeySelective(shipping);
        if(result == 0){
            return  ResponseVo.error(UPDATE_ADDRESS_ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectByUid(uid);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(shippings);
        return ResponseVo.success(pageInfo);
    }
}
