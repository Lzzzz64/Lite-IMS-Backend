package org.example.c.backend.service;

import org.example.c.backend.entity.Customer;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.c.backend.vo.CustomerVO;

import java.util.List;

/**
* @author 123456
* @description 针对表【customer(客户表)】的数据库操作Service
* @createDate 2026-09-29 10:13:58
*/
public interface CustomerService extends IService<Customer> {
    /**
     * 获取客户列表
     * @return
     */
    List<CustomerVO> getCustomerList();
}
