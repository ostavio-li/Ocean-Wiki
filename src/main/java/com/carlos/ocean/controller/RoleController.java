package com.carlos.ocean.controller;

import com.carlos.ocean.mapper.SysUserRoleMapper;
import com.carlos.ocean.pojo.SysUserRole;
import com.carlos.ocean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Carlos Li
 * @date 2021/5/25
 */

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @PutMapping("/{id}")
    public Result updateRole(
            @PathVariable("id") int id,
            @RequestBody Integer[] roles
    ) {
        sysUserRoleMapper.deleteByUserId(id);
        for (Integer role : roles) {

            sysUserRoleMapper.insert(new SysUserRole(id, role));

        }
        return Result.ok();
    }

}
