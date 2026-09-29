package org.example.c.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.c.backend.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 123456
* @description 针对表【category(商品分类表)】的数据库操作Mapper
* @createDate 2026-09-29 10:13:03
* @Entity org.example.c.backend.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




