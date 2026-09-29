package org.example.c.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.c.backend.entity.SaleOrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 123456
* @description 针对表【sale_order_item(订单详情表)】的数据库操作Mapper
* @createDate 2026-09-29 10:14:09
* @Entity org.example.c.backend.entity.SaleOrderItem
*/
@Mapper
public interface SaleOrderItemMapper extends BaseMapper<SaleOrderItem> {

}




