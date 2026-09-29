package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.c.backend.common.Result;
import org.example.c.backend.entity.Category;
import org.example.c.backend.service.CategoryService;
import org.example.c.backend.vo.CategoryVO;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag("商品分类模块")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取商品分类列表")
    public Result<List<CategoryVO>> getCategoryList() {
        return Result.success(categoryService.getCategoryList());
    }

    @PostMapping
    @Operation(summary = "添加商品分类")
    public Result<?> addCategory(@RequestBody Category category) {
        return categoryService.save(category) ?
                Result.success("添加成功", category) :
                Result.fail("添加失败");
    }

    @PutMapping
    @Operation(summary = "修改商品分类")
    public Result<?> updateCategory(@RequestBody Category category) {
        return categoryService.updateById(category) ?
                Result.success("修改成功", category) :
                Result.fail("修改失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品分类")
    public Result<?> deleteCategory(@PathVariable Long id) {
        return categoryService.removeById(id) ?
                Result.success("删除成功",null) :
                Result.fail("删除失败");
    }
}
