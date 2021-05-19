package com.carlos.ocean.repository;

import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.ocean.service.RelatedQuestionService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author EdwardLee
 * @date 2021/5/16
 */

@Repository
public interface RelatedQuestionRepository extends JpaRepository<RelatedQuestion, Integer> {
    List<RelatedQuestion> findAllByTitle(String title);
    List<RelatedQuestion> findAllByTitleAndQuestionNot(String title, String question);
    List<RelatedQuestion> findAllByQuestionLike(String question);
}
