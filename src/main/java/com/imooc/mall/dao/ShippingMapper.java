package com.imooc.mall.dao;

import com.imooc.mall.entity.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    List<Shipping> selectByUid(Integer uid);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int deleteByIdAndUid(@Param("uid") Integer uid,
                         @Param("shippingId") Integer shippingId);

    Shipping selectByUidAndId(@Param("uid") Integer uid,
                              @Param("shippingId") Integer shippingId);
    List<Shipping>selectByShippingIdSet(@Param("shippingId") Set shippingId);
}