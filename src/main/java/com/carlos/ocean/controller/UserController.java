package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.SysUser;
import com.carlos.ocean.service.SysUserService;
import com.carlos.ocean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for SysUser
 * @author Carlos.Li
 * @date 2021/3/3
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private SysUserService sysUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setUserService(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    @PostMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public Result listUsers(
            @RequestParam(value = "name", required = false) String userName,
            @PageableDefault(sort = {"id"}) Pageable pageable
    ) {
//        System.out.println("这是 Mybatis-plus 实现的 list");
//        return ResponseEntity.ok(sysUserService.list());
        return Result.ok().data("users", sysUserService.list());
    }

    @GetMapping(value = "", params = {"id"})
    public ResponseEntity<SysUser> findUser(@RequestParam("id") int id) {
        // TODO: 2021/3/6 按 id 查询 SysUser
        return null;
    }

    @PostMapping("")
    public Result saveUser(
            @RequestBody SysUser sysUser
    ) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        if (sysUserService.save(sysUser)) {
            return Result.ok().data("user", sysUser);
        } else {
            return Result.error().message("用户添加失败");
        }

    }

    @DeleteMapping("")
    public Result deleteUser(
            @RequestParam("id") int id
    ) {
        return Result.ok().data("deleted", sysUserService.removeById(id));
    }

    @PutMapping("")
    public Result updateUser(
            @RequestBody SysUser sysUser
    ) {
        sysUser.setPassword(passwordEncoder.encode(sysUser.getPassword()));
        if (sysUserService.updateById(sysUser)) {
            return Result.ok().data("user", sysUser);
        } else {
            return Result.error().message("用户更新失败");
        }

    }

}
