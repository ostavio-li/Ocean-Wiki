package com.carlos.ocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.ocean.pojo.SysUser;

import java.io.Serializable;
import java.util.List;

/**
 * SysUser Service
 * @author EdwardLee
 * @date 2021/3/5
 */

public interface SysUserService extends IService<SysUser> {

    SysUser getUserByUserName(String userName);
    List<String> getRoleCodeByUsername(String username);
    boolean removeById(Serializable id);

}
