package org.example.c.backend.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@ColumnWidth(20) // 设置默认列宽
public class OrderExportVO {
    @ExcelProperty("订单号")
    private String orderNo;

    @ExcelProperty("客户名称")
    private String customerName;

    @ExcelProperty("订单金额")
    private BigDecimal totalAmount;

    @ExcelProperty("订单状态")
    private String statusStr; // 导出时将 0/1/2 转换为中文

    @ExcelProperty("创建时间")
    @ColumnWidth(45)
    private Date createTime;
}


