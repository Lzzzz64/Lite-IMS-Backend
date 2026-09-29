package org.example.c.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.c.backend.common.Result;
import org.example.c.backend.entity.Customer;
import org.example.c.backend.service.CustomerService;
import org.example.c.backend.vo.CustomerVO;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Tag("客户管理模块")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping
    @Operation(summary = "获取客户列表")
    public Result<List<CustomerVO>> getCustomerList() {
        return Result.success(customerService.getCustomerList());
    }

    @PostMapping
    @Operation(summary = "添加客户")
    public Result<Customer> addCustomer(@RequestBody Customer customer) {
        return customerService.save(customer) ?
                Result.success("添加成功",customer) :
                Result.fail("添加失败");
    }

    @PutMapping
    @Operation(summary = "更新客户")
    public Result<Customer> updateCustomer(@RequestBody Customer customer) {
        return customerService.updateById(customer) ?
                Result.success("更新成功",customer) :
                Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户")
    public Result<?> deleteCustomer(@PathVariable Long id) {
        return customerService.removeById(id) ?
                Result.success("删除成功", id) :
                Result.fail("删除失败");
    }
}
