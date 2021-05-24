package com.carlos.ocean.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.carlos.ocean.mapper.SysUserMapper;
import com.carlos.ocean.pojo.SysUser;
import com.carlos.ocean.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/22
 */

@Service("customerUserDetailsService")
public class CustomerUserDetailsService implements UserDetailsService {

    private SysUserService sysUserService;
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    public void setUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("\n加载用户\n" + username);

        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        SysUser sysUser = this.sysUserMapper.selectOne(wrapper);

//        SysUser sysUser = sysUserService.getUserByUserName(username);

        if (sysUser == null) {
            throw new UsernameNotFoundException("用户名或密码错误!");
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        List<String> roleCodes = sysUserService.getRoleCodeByUsername(username);
        roleCodes.forEach(code -> {
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(code);
            authorities.add(authority);
        });
        sysUser.setAuthorities(authorities);
        System.out.println(sysUser);
        return sysUser;
    }

}
