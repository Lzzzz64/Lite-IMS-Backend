package org.example.c.backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderVO {
    @Schema(description = "订单ID")
    private Long id;
    @Schema(description = "订单编号")
    private String orderNo;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "订单总金额")
    private BigDecimal totalAmount;
    @Schema(description = "订单状态")
    private Integer status;
    @Schema(description = "创建时间")
    private Date createTime;
}
