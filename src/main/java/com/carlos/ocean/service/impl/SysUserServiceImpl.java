package com.carlos.ocean.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carlos.ocean.mapper.SysUserMapper;
import com.carlos.ocean.pojo.SysUser;
import com.carlos.ocean.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/22
 */

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private SysUserMapper sysUserMapper;

    @Override
    public List<SysUser> list() {
        List<SysUser> list = super.list();
        for (SysUser user : list) {
            user.setAuthorities(getAuthoritiesByUsername(user.getUsername()));
        }
        return list;
    }

    @Autowired
    public void setUserMapper(SysUserMapper sysUserMapper) {
        this.sysUserMapper = sysUserMapper;
    }

    @Override
    public SysUser getUserByUserName(String username) {

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);

//        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
//        queryWrapper.lambda().eq(SysUser::getUsername, username);
        return this.sysUserMapper.selectOne(wrapper);
    }

    @Override
    public List<String> getRoleCodeByUsername(String username) {
        return sysUserMapper.getRoleCodeByUsername(username);
    }

    @Override
    public boolean removeById(Serializable id) {
        sysUserMapper.deleteById(id);
        sysUserMapper.deleteRoleByUserId(id);
        return true;
    }

    @Override
    public boolean save(SysUser entity) {
        entity.setPassword(new BCryptPasswordEncoder().encode(entity.getPassword()));
        System.out.println(entity);
        return super.save(entity);
    }

    public SysUser getUserByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return sysUserMapper.selectOne(wrapper);
    }

    @Override
    public Collection<GrantedAuthority> getAuthoritiesByUsername(String username) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roleCodes = getRoleCodeByUsername(username);
        roleCodes.forEach(code -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(code);
            authorities.add(authority);
        });
        return authorities;
    }

}