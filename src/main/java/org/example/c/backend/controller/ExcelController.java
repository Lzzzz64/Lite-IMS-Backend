package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.c.backend.common.Result;
import org.example.c.backend.service.ExcelService;
import org.example.c.backend.service.SaleOrderService;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/excel")
@Tag("Excel导出模块")
public class ExcelController {
    @Autowired
    private ExcelService excelService;

    @GetMapping("/orders/export")
    @Operation(summary = "导出订单数据")
    public Result<?> exportOrders()
    {
        try{
            excelService.exportExcel();
            return Result.success("导出成功");
        }catch (Exception e){
            return Result.fail(e.getMessage());
        }
    }

}
