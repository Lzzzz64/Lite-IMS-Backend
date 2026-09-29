package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.c.backend.common.Result;
import org.example.c.backend.service.DashBoardService;
import org.example.c.backend.vo.DashBoardVO;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag("仪表盘模块")
public class DashboardController {
    @Autowired
    private DashBoardService dashBoardService;

    @GetMapping
    @Operation(summary = "获取统计数据")
    public Result<DashBoardVO> getDashboardData() {
        return Result.success(dashBoardService.getDashBoardData());
    }
}
