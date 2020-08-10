package com.imooc.mall.service;

import com.imooc.mall.entity.Category;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    /**
     * 查询所有数据状态为1的数据
     * @return
     */
    ResponseVo<List<CategoryVo>>selectAll();

    void findSubcategoryById(Integer id, Set<Integer> result);
}
