package com.carlos.ocean.service;

import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.ocean.repository.RelatedQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author EdwardLee
 * @date 2021/5/16
 */

@Service
public class RelatedQuestionService {

    private RelatedQuestionRepository repository;

    @Autowired
    public void setRepository(RelatedQuestionRepository repository) {
        this.repository = repository;
    }

    public List<RelatedQuestion> listAll() {
        return repository.findAll();
    }

    public List<RelatedQuestion> listAllByTitleWithoutQuestion(String title, String question) {
        return repository.findAllByTitleAndQuestionNot(title, question);
    }

}
