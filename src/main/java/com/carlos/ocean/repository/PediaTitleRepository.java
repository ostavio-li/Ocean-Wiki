package com.carlos.ocean.repository;

import com.carlos.ocean.pojo.PediaTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author EdwardLee
 * @date 2021/5/11
 */

@Repository
public interface PediaTitleRepository extends JpaRepository<PediaTitle, Integer> {

    void deleteAllByTitle(String title);

}
