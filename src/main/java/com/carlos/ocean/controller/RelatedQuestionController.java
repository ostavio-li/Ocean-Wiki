package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.ocean.service.RelatedQuestionService;
import org.hibernate.usertype.UserVersionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/17
 */

@RestController
@RequestMapping("/rq")
public class RelatedQuestionController {

    private RelatedQuestionService service;

    @Autowired
    public void setService(RelatedQuestionService service) {
        this.service = service;
    }

    @GetMapping("")
    public ResponseEntity<List<RelatedQuestion>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @GetMapping("/q")
    public ResponseEntity<List<RelatedQuestion>> listRelatedQuestions(
            @RequestParam(value = "question", required = true) String question
    ) {
        // TODO: 2021/5/17 获取相关问题列表
        return ResponseEntity.ok(service.listAllByQuestionLike(question));
    }

}
