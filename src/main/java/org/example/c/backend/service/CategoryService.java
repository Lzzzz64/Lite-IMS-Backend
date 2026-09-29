package org.example.c.backend.service;

import org.example.c.backend.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.c.backend.vo.CategoryVO;

import java.util.List;

/**
* @author 123456
* @description 针对表【category(商品分类表)】的数据库操作Service
* @createDate 2026-09-29 10:13:03
*/
public interface CategoryService extends IService<Category> {
    /**
     * 获取商品分类列表
     * @return
     */
    List<CategoryVO> getCategoryList();
}
