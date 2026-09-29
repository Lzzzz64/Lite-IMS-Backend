package org.example.c.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.c.backend.dto.LoginDTO;
import org.example.c.backend.entity.SysUser;
import org.example.c.backend.service.SysUserService;
import org.example.c.backend.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 123456
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2026-09-29 10:14:10
*/
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService{

    @Override
    public SysUser login(LoginDTO loginDTO) {
        LambdaQueryWrapper<SysUser> lambdaQueryWrapper = new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getUsername, loginDTO.getUsername());
        SysUser sysUser = this.getOne(lambdaQueryWrapper);

        if (sysUser != null && sysUser.getPassword().equals(loginDTO.getPassword())) {
            return sysUser;
        }else{
            return null;
        }
    }
}




