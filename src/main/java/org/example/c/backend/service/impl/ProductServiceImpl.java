package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.entity.Product;
import org.example.c.backend.service.ProductService;
import org.example.c.backend.mapper.ProductMapper;
import org.example.c.backend.vo.PageVO;
import org.example.c.backend.vo.ProductVO;
import org.springframework.stereotype.Service;

/**
* @author 123456
* @description 针对表【product(商品表)】的数据库操作Service实现
* @createDate 2026-09-29 10:14:09
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{
    @Override
    public PageVO<ProductVO> getProducts(Integer page, Integer size, String name, Long categoryId) {
        Page<ProductVO> pageParam = new Page<>(page, size);
        IPage<ProductVO> resultPage = baseMapper.selectProductVOPage(pageParam, name, categoryId);
        return PageVO.convert(resultPage, resultPage.getRecords());
    }
}




