package org.example.c.backend.service;

import org.example.c.backend.dto.OrdersDTO;
import org.example.c.backend.entity.SaleOrder;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.c.backend.entity.SysUser;
import org.example.c.backend.vo.OrderVO;
import org.example.c.backend.vo.PageVO;

import java.util.List;

/**
* @author 123456
* @description 针对表【sale_order(销售订单表)】的数据库操作Service
* @createDate 2026-09-29 10:14:09
*/
public interface SaleOrderService extends IService<SaleOrder> {
    /**
     * 添加订单
     * @param ordersDTO
     * @param user
     * @return
     */
    boolean addOrder(OrdersDTO ordersDTO, SysUser user);

    /**
     * 分页获取订单列表
     * @param page
     * @param size
     * @param orderNo
     * @return
     */
    PageVO<OrderVO> getOrders(Integer page, Integer size, String orderNo);

    /**
     * 更新订单状态
     * @param id
     * @param status
     * @return
     */
    Boolean updateOrderStatus(Long id, Integer status);
}
