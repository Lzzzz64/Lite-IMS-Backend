package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.entity.Category;
import org.example.c.backend.service.CategoryService;
import org.example.c.backend.mapper.CategoryMapper;
import org.example.c.backend.vo.CategoryVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 123456
* @description 针对表【category(商品分类表)】的数据库操作Service实现
* @createDate 2026-09-29 10:13:03
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    /**
     * 获取分类列表
     * @return
     */
    @Override
    public List<CategoryVO> getCategoryList() {
        List<Category> list = this.list();
        List<CategoryVO> categoryVOList = new ArrayList<>();
        for (Category category : list) {
            CategoryVO categoryVO = new CategoryVO();
            categoryVO.setId(category.getId());
            categoryVO.setName(category.getName());
            categoryVO.setSort(category.getSort());
            categoryVOList.add(categoryVO);
        }
        return categoryVOList;
    }
}




