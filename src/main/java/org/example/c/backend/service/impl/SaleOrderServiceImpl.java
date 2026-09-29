package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.dto.OrderItemDTO;
import org.example.c.backend.dto.OrdersDTO;
import org.example.c.backend.entity.Product;
import org.example.c.backend.entity.SaleOrder;
import org.example.c.backend.entity.SaleOrderItem;
import org.example.c.backend.entity.SysUser;
import org.example.c.backend.service.ProductService;
import org.example.c.backend.service.SaleOrderItemService;
import org.example.c.backend.service.SaleOrderService;
import org.example.c.backend.mapper.SaleOrderMapper;
import org.example.c.backend.vo.OrderVO;
import org.example.c.backend.vo.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* @author 123456
* @description 针对表【sale_order(销售订单表)】的数据库操作Service实现
* @createDate 2026-09-29 10:14:09
*/
@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder>
    implements SaleOrderService{
    @Autowired
    private SaleOrderItemService saleOrderItemService;
    @Autowired
    private ProductService productService;

    @Override
    public PageVO<OrderVO> getOrders(Integer page, Integer size, String orderNo) {
        Page<OrderVO> pageParam = new Page<>(page, size);
        IPage<OrderVO> orderVOIPage = baseMapper.selectOrderVOPage(pageParam, orderNo);
        return PageVO.convert(orderVOIPage, orderVOIPage.getRecords());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addOrder(OrdersDTO ordersDTO, SysUser user) {
        SaleOrder saleOrder = new SaleOrder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = sdf.format(new Date());
        int random = (int)(Math.random() * 1000);
        String orderNo = timeStr + random;
        saleOrder.setOrderNo(orderNo);
        saleOrder.setCustomerId(ordersDTO.getCustomerId());
        saleOrder.setUserId(user.getId());
        saleOrder.setStatus(0);
        BigDecimal totalAmount = BigDecimal.ZERO;
        saleOrder.setTotalAmount(totalAmount);
        List<OrderItemDTO> orderItems = ordersDTO.getItems();


        // ========= 第一步：先【校验全部商品库存】，不做扣减（预校验）=========
        if (orderItems == null || orderItems.size() == 0){
            throw new RuntimeException("订单项为空");
        }
        for (OrderItemDTO orderItem : orderItems) {
            Product product = productService.getById(orderItem.getProductId());
            if(product == null){
                throw new RuntimeException("商品不存在");
            }
            if(product.getStock() < orderItem.getQuantity()){
                throw new RuntimeException("商品【"+product.getName()+"】库存不足");
            }
        }

        // ========= 第二步：保存主订单，拿到订单ID =========
        boolean saveOrderOk = save(saleOrder);
        if (!saveOrderOk) {
            throw new RuntimeException("订单创建失败");
        }

        // ========= 第三步：循环扣库存 + 新增订单项 =========
        for (OrderItemDTO orderItem : orderItems) {
            Product product = productService.getById(orderItem.getProductId());

            // 原子扣减库存，数据库层判断库存，防止超卖
            boolean updateOk = productService.lambdaUpdate()
                    .setSql("stock = stock - " + orderItem.getQuantity())
                    .eq(Product::getId, orderItem.getProductId())
                    .ge(Product::getStock, orderItem.getQuantity())
                    .update();
            if (!updateOk) {
                throw new RuntimeException("商品【"+product.getName()+"】库存不足，下单失败");
            }

            // 生成订单项，此时 saleOrder.getId() 已有值！
            SaleOrderItem saleOrderItem = new SaleOrderItem();
            saleOrderItem.setOrderId(saleOrder.getId());
            saleOrderItem.setProductId(orderItem.getProductId());
            saleOrderItem.setQuantity(orderItem.getQuantity());
            saleOrderItem.setPrice(product.getPrice());
            BigDecimal amount = product.getPrice().multiply(new BigDecimal(orderItem.getQuantity()));
            saleOrderItem.setAmount(amount);
            if(!saleOrderItemService.save(saleOrderItem)){
                throw new RuntimeException("生成订单项失败");
            }
            totalAmount = totalAmount.add(amount);
        }

        // 回写订单总金额
        saleOrder.setTotalAmount(totalAmount);
        updateById(saleOrder);

        return true;
    }

}




