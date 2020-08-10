package com.imooc.mall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.entity.Product;
import com.imooc.mall.service.ICategoryService;
import com.imooc.mall.service.IPoductService;
import com.imooc.mall.vo.ProductDetailVo;
import com.imooc.mall.vo.ProductVo;
import com.imooc.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.ProductStasusEnum.DELETE_PRODUCT;
import static com.imooc.mall.enums.ProductStasusEnum.OFF_SELL;
import static com.imooc.mall.enums.ResponseEnum.OFF_SELL_0R_DELETE_PRODUCT;

@Service
@Slf4j
public class ProductServiceImpl implements IPoductService {
    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ResponseVo<PageInfo> list(Integer id, Integer pagNum, Integer pagSize) {
       /* List<ProductVo>productVoList = new ArrayList<>();*/
        Set<Integer> categoryIdSet = new HashSet<>();
        if(id!=null){
            categoryService.findSubcategoryById(id,categoryIdSet);
            categoryIdSet.add(id);
        }
        PageHelper.startPage(pagNum,pagSize);
        List<Product> products = productMapper.selectBycategoryByIdSet(categoryIdSet);
       /* ProductVo productVo = new ProductVo();
        BeanUtils.copyProperties(products,productVo);
        productVoList.add(productVo);*/
       List<ProductVo>productVoList1 = products.stream()
               .map(e->{
                   ProductVo productVo = new ProductVo();
                   BeanUtils.copyProperties(e,productVo);
                   return productVo;
               })
               .collect(Collectors.toList());
        PageInfo pageInfo = new PageInfo(productVoList1);
        pageInfo.setList(productVoList1);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<ProductDetailVo> detail(Integer productId) {
       Product product = productMapper.selectByPrimaryKey(productId);
       if(product.getStatus().equals(OFF_SELL.getCode()) || product.getStatus().equals(DELETE_PRODUCT.getCode())){
          return ResponseVo.error(OFF_SELL_0R_DELETE_PRODUCT);
       }
       ProductDetailVo productDetailVo = new ProductDetailVo();
       //对敏感数据进行处理
       product.setStock(product.getStock()>100?100:product.getStock());
       BeanUtils.copyProperties(product,productDetailVo);
       return ResponseVo.success(productDetailVo);
    }

}
