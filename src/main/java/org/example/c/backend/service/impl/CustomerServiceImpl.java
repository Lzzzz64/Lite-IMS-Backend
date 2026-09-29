package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.entity.Customer;
import org.example.c.backend.service.CustomerService;
import org.example.c.backend.mapper.CustomerMapper;
import org.example.c.backend.vo.CustomerVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author 123456
* @description 针对表【customer(客户表)】的数据库操作Service实现
* @createDate 2026-09-29 10:13:58
*/
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer>
    implements CustomerService{
    @Override
    public List<CustomerVO> getCustomerList() {
        List<Customer> list = this.list();
        ArrayList<CustomerVO> customerVOList = new ArrayList<>();
        for (Customer customer : list) {
            CustomerVO customerVO = new CustomerVO();
            customerVO.setId(customer.getId());
            customerVO.setName(customer.getName());
            customerVO.setPhone(customer.getPhone());
            customerVO.setEmail(customer.getEmail());
            customerVO.setAddress(customer.getAddress());
            customerVOList.add(customerVO);
        }
        return customerVOList;
    }
}




