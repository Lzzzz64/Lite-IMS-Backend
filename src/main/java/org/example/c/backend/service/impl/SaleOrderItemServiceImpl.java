package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.entity.SaleOrderItem;
import org.example.c.backend.service.SaleOrderItemService;
import org.example.c.backend.mapper.SaleOrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author 123456
* @description 针对表【sale_order_item(订单详情表)】的数据库操作Service实现
* @createDate 2026-09-29 10:14:09
*/
@Service
public class SaleOrderItemServiceImpl extends ServiceImpl<SaleOrderItemMapper, SaleOrderItem>
    implements SaleOrderItemService{

}




