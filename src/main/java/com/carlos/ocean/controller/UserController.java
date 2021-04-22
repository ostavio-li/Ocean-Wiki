package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.User;
import com.carlos.ocean.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for User
 * @author Carlos.Li
 * @date 2021/3/3
 */

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("")
    public ResponseEntity<List<User>> listUsers(
            @RequestParam(value = "name", required = false) String userName,
            @PageableDefault(sort = {"id"}) Pageable pageable
    ) {
        return ResponseEntity.ok(userService.listUsers(userName, pageable));
    }

    @GetMapping(value = "", params = {"id"})
    public ResponseEntity<User> findUser(@RequestParam("id") int id) {
        // TODO: 2021/3/6 按 id 查询 User
        return null;
    }

    @PostMapping("")
    public ResponseEntity<User> saveUser(
            @RequestBody @Validated User user
    ) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.CREATED);
    }

    @DeleteMapping("")
    public ResponseEntity<Boolean> deleteUser(
            @RequestParam("id") int id
    ) {
        return ResponseEntity.ok(userService.deleteUser(id));
    }

    @PutMapping("")
    public ResponseEntity<User> updateUser(
            @RequestBody User user
    ) {
        return new ResponseEntity<>(userService.updateUser(user), HttpStatus.CREATED);
    }

}
