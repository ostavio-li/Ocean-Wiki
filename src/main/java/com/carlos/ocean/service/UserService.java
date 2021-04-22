package com.carlos.ocean.service;

import com.carlos.ocean.pojo.User;
import com.carlos.ocean.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * User Service
 * @author EdwardLee
 * @date 2021/3/5
 */

@Service
public class UserService {

    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> listUsers(String userName, Pageable pageable) {
        userName = "%" + (userName == null ? "" : userName) + "%";
        return repository.findAllByUserNameLike(userName, pageable);
    }

    public User saveUser(User user) {

        if (user == null) {
            return null;
        }
        return repository.save(user);

    }

    @Transactional
    public Boolean deleteUser(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public User updateUser(User user) {
        return repository.save(user);
    }

}
