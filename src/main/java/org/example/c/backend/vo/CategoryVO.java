package org.example.c.backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Data
public class CategoryVO {
    @Schema(description = "分类ID")
    private Long id;
    @Schema(description = "分类名称")
    private String name;
    @Schema(description = "分类排序")
    private Integer sort;
}
