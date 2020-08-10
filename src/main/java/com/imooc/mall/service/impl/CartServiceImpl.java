package com.imooc.mall.service.impl;

import com.google.gson.Gson;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.entity.Cart;
import com.imooc.mall.entity.Product;
import com.imooc.mall.enums.ProductStasusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.vo.CartProductVo;
import com.imooc.mall.vo.CartVo;
import com.imooc.mall.vo.ResponseVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static com.imooc.mall.enums.ResponseEnum.PRODUCT_NOT_EXIST;

@Service
public class CartServiceImpl implements ICartService {
    public static final String CART_REDIS_KEY_TEMPLATE ="cart_%d";
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();
    @Override
    /**
     * 增加购物车商品
     */
    public ResponseVo add(Integer uid,CartAddForm cartAddForm) {
        Integer quantity=1;
        Product product = productMapper.selectByPrimaryKey(cartAddForm.getProductId());
        //商品是否存在
        if(product ==null){
            return ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        //商品是否正常在售
        if(!product.getStatus().equals(ProductStasusEnum.ON_SELE.getCode()) ){
            return ResponseVo.error(ResponseEnum.OFF_SELL_0R_DELETE_PRODUCT);
        }
       // 商品是否充足
        if(product.getStock()<=0){
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        //写入到redis
        //key :cart_1
        Cart cart;
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = hashOperations.get(redisKey,String.valueOf(product.getId()));
        if(StringUtils.isEmpty(value)){
             cart = new Cart(product.getId(),quantity, cartAddForm.getSelected());
        }else {
            cart = gson.fromJson(value,Cart.class);
           cart.setQuantity(cart.getQuantity()+quantity);
        }
        hashOperations.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));
         /* redisTemplate.opsForValue().set(String.format(CART_REDIS_KEY_TEMPLATE,uid),
                gson.toJson(new Cart(product.getId(),quantity,cartAddForm.getSelected())));*/

     /*  HashOperations<String,String,String>opsForHash = redisTemplate.opsForHash();
       opsForHash.put(String.format(CART_REDIS_KEY_TEMPLATE,uid),
                      String.valueOf(product.getId()),
                       gson.toJson(new Cart(product.getId(),quantity,cartAddForm.getSelected())));*/
        return ResponseVo.success();
    }

    @Override
    /**
     * 显示购物车商品列表
     */
    public ResponseVo<CartVo> list(Integer uid) {
        HashOperations<String,String,String> operations = redisTemplate.opsForHash();///连接redis，斌获取redis信息
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String,String>entries = operations.entries(redisKey);//通过uid,获取redis中信息
        CartVo cartVo = new CartVo();
        boolean selectedAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        Set<Integer>productId1 = new HashSet<>();
        List<Cart> cartList = new ArrayList<>();
        for(Map.Entry<String,String> entry :entries.entrySet()){
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);//将查处来的信息对象化
            productId1.add(productId);
            cartList.add(cart);
            //TODO 需要优化，最好不要在循环中查询数据库，使用mysql  in
       /*    Product product = productMapper.selectByPrimaryKey(productId);
           if(product !=null){
               CartProductVo cartProductVo = new CartProductVo(productId,
                       cart.getQuantity(),
                       product.getName(),
                       product.getSubtitle(),
                       product.getMainImage(),
                       product.getPrice(),
                       product.getStatus(),
                       product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                       product.getStock(),
                       cart.getProductSelected());
               cartProductVoList.add(cartProductVo);
               //全部选中才叫全选
               if(!cart.getProductSelected()) {
                   selectedAll = false;
               }
               //只计算选中的
               if(cart.getProductSelected() == true){
                   cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
               }
           }
            cartTotalQuantity += cart.getQuantity();*/
        }
        List<Product> productList = productMapper.selectByproductIdSet(productId1);
        if(productList !=null) {
            for (Product productVo : productList) {
                for (Cart cart : cartList) {
                    /*cartProductVo.setProductId(productVo.getId());
                    cartProductVo.setQuantity(cart.getQuantity());
                    cartProductVo.setProductName(productVo.getName());
                    cartProductVo.setProductSubtitle(productVo.getSubtitle());
                    cartProductVo.setProductMainImage(productVo.getMainImage());
                    cartProductVo.setProductPrice(productVo.getPrice());
                    cartProductVo.setProductStatus(productVo.getSutats());
                    cartProductVo.setProductTotalPrice(productVo.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
                    cartProductVo.setProductStock(productVo.getStock());
                    cartProductVo.getProductSelected();*/
                    CartProductVo cartProductVo = new CartProductVo(productVo.getId(),
                            cart.getQuantity(),
                            productVo.getName(),
                            productVo.getSubtitle(),
                            productVo.getMainImage(),
                            productVo.getPrice(),
                            productVo.getStatus(),
                            productVo.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                            productVo.getStock(),cart.getProductSelected());
                    cartProductVoList.add(cartProductVo);
                    //全部选中才叫全选
                    if (!cart.getProductSelected()) {
                        selectedAll = false;
                    }
                    //只计算选中的
                    if (cart.getProductSelected() == true) {
                        cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                    }
                    cartTotalQuantity += cart.getQuantity();
                }

            }
            cartVo.setSelectedAll(selectedAll);
            cartVo.setCartTotalQuantity(cartTotalQuantity);
            cartVo.setCartTotalPrice(cartTotalPrice);
            cartVo.setCartProductVoList(cartProductVoList);
        }
        return ResponseVo.success(cartVo);
    }

    @Override
    /**
     * 更新购物车商品
     */
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm cartUpdateForm) {
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = hashOperations.get(redisKey,String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品报错
           return ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        Cart cart = gson.fromJson(value,Cart.class);
        if(cartUpdateForm.getQuantity()!=null&&cartUpdateForm.getQuantity()>=0){
            cart.setQuantity(cartUpdateForm.getQuantity());
        }
        if(cartUpdateForm.getSelect()!=null){
            cart.setProductSelected(cartUpdateForm.getSelect());
        }
        hashOperations.put(redisKey,String.valueOf(productId),gson.toJson(cart));
        return list(uid);
    }

    @Override
    /**
     * 删除购物车商品
     */
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String,String,String> hashOperations = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = hashOperations.get(redisKey,String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品报错
            return ResponseVo.error(PRODUCT_NOT_EXIST);
        }
        hashOperations.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    @Override
    /**
     * 全选
     */
    public ResponseVo<CartVo> sellectAll(Integer uid) {
        HashOperations<String,String,String> operations = redisTemplate.opsForHash();///连接redis，斌获取redis信息
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for(Cart cart :listForCart(uid)){
            cart.setProductSelected(true);
            operations.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    /**
     * 全不选中
     */
    public ResponseVo<CartVo> unSellectAll(Integer uid) {
        HashOperations<String,String,String> operations = redisTemplate.opsForHash();///连接redis，斌获取redis信息
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for(Cart cart :listForCart(uid)){
            cart.setProductSelected(false);
            operations.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    /**
     * 返回购物车商品数量
     */
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream().
                map(Cart::getQuantity)
                .reduce(0,Integer::sum);
        return ResponseVo.success(sum);
    }
    @Override
    /**
     * 查询购物车商品列表
     */
    public List<Cart>listForCart(Integer uid){
        HashOperations<String,String,String> operations = redisTemplate.opsForHash();///连接redis，斌获取redis信息
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String,String>entries = operations.entries(redisKey);//通过uid,获取redis中信息
        List<Cart>cartList = new ArrayList<>();
        for(Map.Entry<String,String> entry:entries.entrySet()){
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return  cartList;
    }
}

