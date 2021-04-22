package com.carlos.ocean.repository;

import com.carlos.ocean.pojo.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for User
 * @author Carlos.Li
 * @date 2021/3/5
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 按 UserName 精确查询
     * @param userName 目标用户名
     * @return 目标用户List
     */
//    List<User> findAllByUserName(String userName);

    List<User> findAllByUserNameLike(String userName, Pageable pageable);

//    List<String> findIdByUserNameLike(String userName);


}
