package com.imooc.mall.dao;

import com.imooc.mall.entity.Order;
import com.sun.org.apache.xpath.internal.operations.Or;

import java.util.List;

public interface OrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKeyWithBLOBs(Order record);

    int updateByPrimaryKey(Order record);

    List<Order> selectByUid(Integer uid);

    Order selectByOrderNo(Long id);
}