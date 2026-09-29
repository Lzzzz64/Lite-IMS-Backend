package org.example.c.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OrderItemDTO {
    @Schema(description = "商品ID")
    private Long productId;
    @Schema(description = "商品数量")
    private Integer quantity;
}
