package org.example.c.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Data
public class LoginDTO {
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "密码")
    @ToString.Exclude
    private String password;
}
