package com.imooc.mall.service;



import com.imooc.mall.MallApplication;
import com.imooc.mall.MallApplicationTests;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.service.impl.CategoryServiceImpl;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class ICategoryServiceTest extends MallApplicationTests {
    @Autowired
    private ICategoryService categoryService;
    @Test
  public void selectAll(){
      ResponseVo<List<CategoryVo> >responseVo = categoryService.selectAll();
      Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
    @Test
  public void findSubcategoryById(){
        Set<Integer> result = new HashSet();
        categoryService.findSubcategoryById(100001,result);
        log.info("result=:{}",result);
  }
}