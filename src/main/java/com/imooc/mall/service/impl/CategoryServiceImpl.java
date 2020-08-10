package com.imooc.mall.service.impl;

import com.imooc.mall.dao.CategoryMapper;
import com.imooc.mall.entity.Category;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.vo.CategoryVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.imooc.mall.consts.CategoryConsta.ROOT_PARENT_ID;

@Service
@Slf4j
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    //耗时排序 1 http请求，2内网+磁盘3，内存
    public ResponseVo<List<CategoryVo>> selectAll() {
     List<CategoryVo> categoryVoList = new ArrayList<>();
     List<Category> category= categoryMapper.selectAll();
      for(Category categories : category){
        if(categories.getParentId().equals(ROOT_PARENT_ID)){
            CategoryVo categoryVo = new CategoryVo();
            BeanUtils.copyProperties(categories,categoryVo);
            categoryVoList.add(categoryVo);
            //一级目录排序
            categoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
        }
      }
      //查询子目录
       findsubCategory(categoryVoList,category);
        return ResponseVo.success(categoryVoList);
    }

    @Override
    public void findSubcategoryById(Integer id, Set<Integer>result) {
        List<Category> category= categoryMapper.selectAll();
        findSubcategoryById(id,result,category);
    }
    public void findSubcategoryById(Integer id, Set<Integer>result,List<Category> category) {
        for(Category category1 :category){
            if(category1.getParentId().equals(id)){
                result.add(category1.getId());
                findSubcategoryById(category1.getId(),result,category);
            }
        }
    }
    public void findsubCategory(List<CategoryVo> categoryVoList,List<Category> category){
        for(CategoryVo categoryVo :categoryVoList){
            List<CategoryVo> categoryVoList1 = new ArrayList<>();
            for(Category category1: category){
                if(categoryVo.getId().equals(category1.getParentId())){
                    CategoryVo categoryVo1 = new CategoryVo();
                    BeanUtils.copyProperties(category1,categoryVo1);
                    categoryVoList1.add(categoryVo1);
                    //子级目录排序
                    categoryVoList1.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());
                    categoryVo.setSubCategory(categoryVoList1);
                    findsubCategory(categoryVoList1,category);
                }

            }

        }
    }
}
