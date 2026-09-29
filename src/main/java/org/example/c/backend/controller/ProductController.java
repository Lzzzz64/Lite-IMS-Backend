package org.example.c.backend.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import org.example.c.backend.common.Result;
import org.example.c.backend.entity.Product;
import org.example.c.backend.service.ProductService;
import org.example.c.backend.vo.PageVO;
import org.example.c.backend.vo.ProductVO;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/products")
@Tag("商品管理模块")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    @Operation(summary = "分页获取商品列表")
    public Result<PageVO<ProductVO>> getProducts(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                 @RequestParam(value = "name", required = false) String name,
                                                 @RequestParam(value = "categoryId", required = false) Long categoryId) {
        PageVO<ProductVO> productPage = productService.getProducts(page, size, name, categoryId);
        return Result.success("获取成功", productPage);
    }

    @PostMapping
    @Tag("添加商品")
    public Result<Product> addProduct(@RequestBody Product product) {
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        return productService.save(product) ?
                Result.success("添加成功", product) :
                Result.fail("添加失败") ;
    }

    @PutMapping
    @Tag("修改商品")
    public Result<Product> updateProduct(@RequestBody Product product) {
        product.setUpdateTime(new Date());
        return productService.updateById(product) ?
                Result.success("修改成功", product) :
                Result.fail("修改失败") ;
    }
    @DeleteMapping("/{id}")
    @Tag("删除商品")
    public Result<?> deleteProduct(@PathVariable Long id) {
        return productService.removeById(id) ?
                Result.success("删除成功", id) :
                Result.fail("删除失败") ;
    }
}
