package org.example.c.backend.service;

import org.example.c.backend.dto.LoginDTO;
import org.example.c.backend.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 123456
* @description 针对表【sys_user(系统用户表)】的数据库操作Service
* @createDate 2026-09-29 10:14:10
*/
public interface SysUserService extends IService<SysUser> {
    /**
     * 用户登录
     * @param loginDTO
     * @return
     */
    SysUser login(LoginDTO loginDTO);
}
