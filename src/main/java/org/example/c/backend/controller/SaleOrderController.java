package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.example.c.backend.common.Result;
import org.example.c.backend.dto.OrdersDTO;
import org.example.c.backend.entity.SysUser;
import org.example.c.backend.service.SaleOrderService;
import org.example.c.backend.vo.OrderVO;
import org.example.c.backend.vo.PageVO;
import org.example.c.backend.vo.ProductVO;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@Tag("订单管理模块")
public class SaleOrderController {
    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping
    @Operation(summary = "分页获取商品列表")
    public Result<PageVO<OrderVO>> getOrders(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                               @RequestParam(value = "orderNo", required = false) String orderNo) {
        PageVO<OrderVO> orderPage = saleOrderService.getOrders(page, size, orderNo);
        return Result.success("获取成功", orderPage);
    }
    @PostMapping
    @Operation(summary = "添加订单")
    public Result<?> addOrder(@RequestBody OrdersDTO ordersDTO, HttpSession session) {
        SysUser user = (SysUser) session.getAttribute("user");

        try {
            saleOrderService.addOrder(ordersDTO, user);
            // 成功不需要返回原始dto，或者你可以在service返回订单VO给前端
            return Result.success("订单添加成功");
        } catch (RuntimeException e) {
            // 业务异常，返回具体提示（库存不足、商品不存在等）
            e.printStackTrace();
            return Result.fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("服务器异常，下单失败");
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新订单状态")
    public Result<?> updateOrderStatus(@PathVariable("id") Long id, @RequestParam("status") Integer status) {
        return saleOrderService.updateOrderStatus(id, status) ?
                Result.success("更新成功", id) :
                Result.fail("更新失败");
    }
}
