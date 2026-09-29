package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.c.backend.entity.Product;
import org.example.c.backend.entity.SaleOrder;
import org.example.c.backend.service.DashBoardService;
import org.example.c.backend.service.ProductService;
import org.example.c.backend.service.SaleOrderService;
import org.example.c.backend.vo.DashBoardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class DashBoardServiceImpl implements DashBoardService {
    @Autowired
    private ProductService productService;
    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    public DashBoardVO getDashBoardData() {
        DashBoardVO dashBoardVO = new DashBoardVO();
        //商品总数
        List<Product> list = productService.list();
        dashBoardVO.setTotalProducts((long) list.size());

        //今日订单数
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime startOfNextDay = LocalDate.now().plusDays(1).atStartOfDay();
        LambdaQueryWrapper<SaleOrder> lambdaQueryWrapper = new LambdaQueryWrapper<SaleOrder>()
                .ge(SaleOrder::getCreateTime, startOfDay)
                .lt(SaleOrder::getCreateTime, startOfNextDay);
        List<SaleOrder> saleOrderList = saleOrderService.list(lambdaQueryWrapper);
        dashBoardVO.setTodayOrders((long) saleOrderList.size());

        //今日销售额
        BigDecimal totalSales = BigDecimal.ZERO;
        for(SaleOrder saleOrder : saleOrderList){
            totalSales = totalSales.add(saleOrder.getTotalAmount());
        }
        dashBoardVO.setTotalSales(totalSales);

        return dashBoardVO;
    }
}
