package com.carlos.ocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.ocean.pojo.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * SysUser Mapper
 * @author Carlos Li
 * @date 2021/5/21
 */

public interface SysUserMapper extends BaseMapper<SysUser> {
    List<String> getRoleCodeByUsername(String username);
    boolean deleteRoleByUserId(Serializable id);
}
