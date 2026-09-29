package org.example.c.backend.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.c.backend.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 123456
* @description 针对表【sys_user(系统用户表)】的数据库操作Mapper
* @createDate 2026-09-29 10:14:10
* @Entity org.example.c.backend.entity.SysUser
*/
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}




