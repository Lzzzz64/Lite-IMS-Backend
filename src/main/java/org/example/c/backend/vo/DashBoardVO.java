package org.example.c.backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashBoardVO {
    @Schema(description = "商品总数")
    private Long totalProducts;
    @Schema(description = "今日订单数")
    private Long todayOrders;
    @Schema(description = "今日销售额")
    private BigDecimal totalSales;
}
