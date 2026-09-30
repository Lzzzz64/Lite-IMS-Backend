package org.example.c.backend.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.c.backend.entity.Customer;
import org.example.c.backend.entity.SaleOrder;
import org.example.c.backend.service.CustomerService;
import org.example.c.backend.service.ExcelService;
import org.example.c.backend.service.SaleOrderService;
import org.example.c.backend.vo.OrderExportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelServiceImpl implements ExcelService {
    @Autowired
    private SaleOrderService saleOrderService;

    @Autowired
    private CustomerService customerService;

    @Override
    public Boolean exportExcel() throws RuntimeException {
        List<OrderExportVO> orderExportVOList = new ArrayList<>();

        List<SaleOrder> saleOrderlist = saleOrderService.list();
        for (SaleOrder saleOrder : saleOrderlist) {
            OrderExportVO orderExportVO = new OrderExportVO();
            // 设置订单号
            orderExportVO.setOrderNo(saleOrder.getOrderNo());
            // 设置客户名称
            LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Customer::getId, saleOrder.getCustomerId());
            Customer customer = customerService.getOne(lambdaQueryWrapper);
            orderExportVO.setCustomerName(customer.getName());
            // 设置订单金额
            orderExportVO.setTotalAmount(saleOrder.getTotalAmount());
            // 设置订单状态
            Integer status = saleOrder.getStatus();
            switch (status) {
                case 1:
                    orderExportVO.setStatusStr("已完成");
                    break;
                case 2:
                    orderExportVO.setStatusStr("已取消");
                    break;
                default:
                    orderExportVO.setStatusStr("待处理");
            }
            orderExportVO.setCreateTime(saleOrder.getCreateTime());
            orderExportVOList.add(orderExportVO);
        }
        EasyExcel.write("D:\\IntelliJ IDEA 2024.1\\workspace\\20260929\\backend\\order.xlsx", OrderExportVO.class).sheet("测试").doWrite(orderExportVOList);
        return true;
    }
}
