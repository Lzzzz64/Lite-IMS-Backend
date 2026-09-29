package org.example.c.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDTO {
    @Schema(description = "客户ID")
    private Long customerId;
    @Schema(description = "订单项列表")
    private List<OrderItemDTO> items;
}
