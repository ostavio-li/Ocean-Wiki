package com.carlos.ocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.ocean.pojo.SysUser;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
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
    SysUser getUserByUsername(String username);
    Collection<GrantedAuthority> getAuthoritiesByUsername(String username);
}
