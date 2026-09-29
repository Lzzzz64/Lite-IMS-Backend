package org.example.c.backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductVO {
    @Schema(description = "商品ID")
    private Long id;
    @Schema(description = "商品名称")
    private String name;
    @Schema(description = "商品价格")
    private BigDecimal price;
    @Schema(description = "商品库存")
    private Integer stock;
    @Schema(description = "商品分类名称")
    private String categoryName;
}
