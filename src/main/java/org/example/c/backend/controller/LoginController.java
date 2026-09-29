package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import org.example.c.backend.common.Result;
import org.example.c.backend.dto.LoginDTO;
import org.example.c.backend.entity.SysUser;
import org.example.c.backend.service.SysUserService;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag("登录模块")
public class LoginController {
    @Autowired
    private SysUserService sysUserService;

    @Operation(summary = "登录接口")
    @PostMapping("/login")
    public Result<SysUser> login(@RequestBody LoginDTO loginDTO, HttpSession session) {
        SysUser sysUser = sysUserService.login(loginDTO);

        if(sysUser == null) {
            return Result.fail(401,"用户名或密码错误");
        }

        // 密码只用于本次身份校验，不写入 Session，也不返回给客户端
        sysUser.setPassword(null);
        session.setAttribute("user", sysUser);
        return Result.success("登录成功", sysUser);
    }

    @Operation(summary = "退出登录接口")
    @GetMapping("/logout")
    public Result<?> logout(HttpSession session) {
        session.invalidate(); // 销毁 Session
        return Result.success("退出成功", null);
    }
}
