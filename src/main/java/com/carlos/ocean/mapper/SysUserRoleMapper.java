package com.carlos.ocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.ocean.pojo.SysUserRole;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/25
 */

public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    List<SysUserRole> selectAllByUserIdAndRoleId(int userId, int roleId);

    int deleteByUserId(int userId);

}