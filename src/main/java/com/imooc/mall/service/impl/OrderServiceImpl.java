package com.imooc.mall.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.mall.dao.OrderItemMapper;
import com.imooc.mall.dao.OrderMapper;
import com.imooc.mall.dao.ProductMapper;
import com.imooc.mall.dao.ShippingMapper;
import com.imooc.mall.entity.*;
import com.imooc.mall.enums.OrderStatusEnum;
import com.imooc.mall.enums.ProductStasusEnum;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.service.ICartService;
import com.imooc.mall.service.IOrderService;
import com.imooc.mall.vo.OrderItemVo;
import com.imooc.mall.vo.OrderVo;
import com.imooc.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.imooc.mall.enums.OrderStatusEnum.NO_PAY;
import static com.imooc.mall.enums.PaymentTypeEnum.PAY_ONLINE;
import static com.imooc.mall.enums.ResponseEnum.ADDRESS_NOT_EXIST;
import static com.imooc.mall.enums.ResponseEnum.CART_NOT_SELECT;
import static com.imooc.mall.enums.ResponseEnum.ERROR;

@Service
@Transactional
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Override
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //收货地址校验（都要查出来）
        Shipping shipping = shippingMapper.selectByUidAndId(uid, shippingId);
        if(shipping == null){
            return  ResponseVo.error(ADDRESS_NOT_EXIST);
        }
        //获取购物车，校验（是否有商品，库存）
        List<Cart> cartlist = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if(CollectionUtils.isEmpty(cartlist)){
            return ResponseVo.error(CART_NOT_SELECT);
        }
        //获取购物车列表里面的productId
        /*Set<Integer>productIdSet = cartlist.stream()
                                    .map(Cart::getProductId)
                                    .collect(Collectors.toSet());*/
        Set<Integer>productIdSet = new HashSet<>();
        for(Cart productId :cartlist){
            Integer productId1 = productId.getProductId();
            productIdSet.add(productId1);
        }
         List<Product>productList = productMapper.selectByproductIdSet(productIdSet);
         Map<Integer,Product>productMap = productList.stream()
                                          .collect(Collectors.toMap(Product::getId,product -> product));
         List<OrderItem>orderItemList = new ArrayList<>();
         Long orderNo = generateOrderNo();
        for (Cart cart :cartlist){
            Product product = productMap.get(cart.getProductId());
            //是否有商品
            if(product == null){
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST,"商品不存在"+"productId="+cart.getProductId());
            }
            //商品上架状态
            if(!ProductStasusEnum.ON_SELE.getCode().equals(product.getStatus())){
                return ResponseVo.error(ResponseEnum.OFF_SELL_0R_DELETE_PRODUCT,"商品不是在售状态"+product.getName());
            }
            //库存是否充足
            if(cart.getQuantity()>product.getStock()){
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR,"库存不正确"+product.getName());
            }

            OrderItem orderItem = BuildOrderItem(uid, orderNo, product, cart);
            orderItemList.add(orderItem);
            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int rowForProduct = productMapper.updateByPrimaryKeySelective(product);
            if(rowForProduct <=0){
                return ResponseVo.error(ResponseEnum.ERROR);
            }

        }
        // 计算总价
        //生成订单，入库：order和orderItem事务
        Order order =buildOrder(uid,orderNo,shippingId,orderItemList);
        int rowForOrder = orderMapper.insertSelective(order);
        if(rowForOrder <=0){
            return ResponseVo.error(ResponseEnum.ERROR );
        }
        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);
        if(rowForOrderItem <=0){
            return ResponseVo.error(ResponseEnum.ERROR );
        }

        //更新购物车（选中商品）
        for(Cart cart :cartlist){
            cartService.delete(uid,cart.getProductId());
        }
        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNumm, Integer pageSize) {
        PageHelper.startPage(pageNumm,pageSize);
        List<Order>orderList = orderMapper.selectByUid(uid);
       /* Set<Long>orderNoSet = orderList.stream()
                            .map(Order::getOrderNo)
                            .collect(Collectors.toSet());*/
        Set<Long>orderNoSet = new HashSet<>();
        for(Order orderId :orderList){
            Long orderId1 = orderId.getOrderNo();
            orderNoSet.add(orderId1);
        }
        List<OrderItem> orderItems = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>>orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));
        Set<Integer>orderShippingId = orderList.stream()
                                    .map(Order::getShippingId)
                                    .collect(Collectors.toSet());
        List<Shipping> shippings = shippingMapper.selectByShippingIdSet(orderShippingId);
        List<OrderVo>orderVoList = new ArrayList<>();
        Map<Integer,Shipping> shippingMap = shippings.stream()
                                    .collect(Collectors.toMap(Shipping::getId,shipping -> shipping));
        for(Order order :orderList){
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return  ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Set<Long>orderNoSet =new HashSet<>();
        orderNoSet.add(orderNo);
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    @Override
    public ResponseVo concel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null || !order.getUserId().equals(uid)){
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //只能取消未支付的订单
        if(!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if(row <=0){
            return ResponseVo.error(ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public void payId(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if(order == null){
            throw  new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc()+"订单Id"+orderNo);
        }
        //只能取消未支付的订单
        if(!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            throw  new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc()+"订单Id"+orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if(row <=0){
           throw  new RuntimeException(ResponseEnum.ERROR.getDesc());
        }
    }

    private OrderVo buildOrderVo(Order order,
                                  List<OrderItem> orderItemList,
                                  Shipping shipping) {
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for(OrderItem orderItemVoList1 :orderItemList){
            OrderItemVo orderItemVo = new OrderItemVo();
            orderItemVo.setOrderNo(orderItemVoList1.getOrderNo());
            orderItemVo.setProductId(orderItemVoList1.getProductId());
            orderItemVo.setProductName(orderItemVoList1.getProductName());
            orderItemVo.setProductImage(orderItemVoList1.getProductImage());
            orderItemVo.setCurrentUnitPrice(orderItemVoList1.getCurrentUnitPrice());
            orderItemVo.setQuantity(orderItemVoList1.getQuantity());
            orderItemVo.setTotalPrice(orderItemVoList1.getTotalPrice());
            orderItemVo.setCreateTime(orderItemVoList1.getCreateTime());
            orderItemVoList.add(orderItemVo);
        }
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        orderVo.setOrderItemVoList(orderItemVoList);
        if(shipping !=null){
            orderVo.setShippinngId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }

        return orderVo;
    }

    private Order buildOrder(Integer uid,Long orderNo,Integer shippingId,List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream()
                            .map(OrderItem::getTotalPrice)
                            .reduce(BigDecimal.ZERO,BigDecimal::add);
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(NO_PAY.getCode());
        return order;
    }

    /**
     * 分布式唯一Id
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis()+new Random().nextInt(999);
    }

    private OrderItem BuildOrderItem(Integer uid,Long orderNo,Product product,Cart cart) {
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(cart.getQuantity());
        orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())));
        return orderItem;
    }
}
