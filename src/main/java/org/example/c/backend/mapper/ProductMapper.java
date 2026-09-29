package org.example.c.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.c.backend.entity.Product;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.example.c.backend.vo.ProductVO;

/**
* @author 123456
* @description 针对表【product(商品表)】的数据库操作Mapper
* @createDate 2026-09-29 10:14:09
* @Entity org.example.c.backend.entity.Product
*/
@Mapper
public interface ProductMapper extends BaseMapper<Product> {
    IPage<ProductVO> selectProductVOPage(IPage<ProductVO> page, @Param("name") String name, @Param("categoryId") Long categoryId);
}




