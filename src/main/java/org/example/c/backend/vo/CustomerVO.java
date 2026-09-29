package org.example.c.backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CustomerVO {
    @Schema(description = "客户ID")
    private Long id;
    @Schema(description = "客户名称")
    private String name;
    @Schema(description = "客户电话")
    private String phone;
    @Schema(description = "客户地址")
    private String address;
    @Schema(description = "客户邮箱")
    private String email;
}
