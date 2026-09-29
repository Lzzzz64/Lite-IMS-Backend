package org.example.c.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.c.backend.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.c.backend.vo.PageVO;
import org.example.c.backend.vo.ProductVO;

/**
* @author 123456
* @description 针对表【product(商品表)】的数据库操作Service
* @createDate 2026-09-29 10:14:09
*/
public interface ProductService extends IService<Product> {
    /**
     * 分页获取商品列表
     * @param page
     * @param size
     * @param name
     * @param categoryId
     * @return
     */
    PageVO<ProductVO> getProducts(Integer page, Integer size, String name, Long categoryId);
}
