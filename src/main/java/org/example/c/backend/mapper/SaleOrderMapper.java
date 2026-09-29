package org.example.c.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.c.backend.entity.SaleOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.c.backend.vo.OrderVO;
import org.example.c.backend.vo.ProductVO;

/**
* @author 123456
* @description 针对表【sale_order(销售订单表)】的数据库操作Mapper
* @createDate 2026-09-29 10:14:09
* @Entity org.example.c.backend.entity.SaleOrder
*/
@Mapper
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {
    IPage<OrderVO> selectOrderVOPage(IPage<OrderVO> page, @Param("orderNo") String orderNo);
}




