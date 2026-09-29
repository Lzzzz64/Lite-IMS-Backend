package org.example.c.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.c.backend.entity.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 123456
* @description 针对表【customer(客户表)】的数据库操作Mapper
* @createDate 2026-09-29 10:13:58
* @Entity org.example.c.backend.entity.Customer
*/
@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {

}




