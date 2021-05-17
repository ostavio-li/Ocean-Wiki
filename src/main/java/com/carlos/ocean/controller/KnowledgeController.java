package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.PediaTitle;
import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.ocean.service.KGService;
import com.carlos.ocean.service.PediaTitleService;
import com.carlos.ocean.service.RelatedQuestionService;
import com.carlos.util.http.HttpUtil;
import com.carlos.util.kg.KGUtil;
import com.carlos.util.nlp.NLPUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author EdwardLee
 * @date 2021/3/30
 */

@RestController
@RequestMapping("/kg")
public class KnowledgeController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private KGService service;
    private PediaTitleService pediaTitleService;
    private RelatedQuestionService relatedQuestionService;

    @Autowired
    public void setService(KGService service) {
        this.service = service;
    }

    @Autowired
    public void setPediaTitleService(PediaTitleService pediaTitleService) {
        this.pediaTitleService = pediaTitleService;
    }

    @Autowired
    public void setRelatedQuestionService(RelatedQuestionService relatedQuestionService) {
        this.relatedQuestionService = relatedQuestionService;
    }

    @DeleteMapping("")
    public ResponseEntity<Void> clear() {
        KGUtil.getInstance().clear();
        pediaTitleService.clearAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Void> buildKg(
            @RequestParam("title") String kgTitle,
            @RequestParam("recurr") int recurrence
    ) {
        logger.info("Recurrence: " + recurrence);
        System.out.println("KGTitle: " + kgTitle);
        try {
            KGUtil.getInstance().build(kgTitle, recurrence);
        } catch (InterruptedException e) {
            return new ResponseEntity<>(HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/pause")
    public ResponseEntity<Void> pauseBuild() {
        service.pauseBuild();
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @PutMapping("/sim")
    public ResponseEntity<Void> updateSimilarity(
            @RequestParam("similarity") double similarity
    ) {
        if (service.updateSimilarity(similarity)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/sim")
    public ResponseEntity<Double> getSimilarity() {
        return ResponseEntity.ok(service.getSimilarity());
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchForAnswer(
            @RequestParam("question") String question
    ) {
        String[] parsed = NLPUtil.parseQuestion(question);
        String title = parsed[0];
        // 获取 文章
        String article = HttpUtil.findArticleByTitle(title);
        String findTitle = title;
        if (article == null || article.length() == 0) {
            findTitle = HttpUtil.findTitle(title);
            if (findTitle != null) {
                article = HttpUtil.findArticleByTitle(findTitle);
            } else {
                article = "";
            }
        }
        List<RelatedQuestion> relatedQuestions = relatedQuestionService.listAllByTitleWithoutQuestion(findTitle, question);
        Map<String, Object> map = new HashMap<>(4);
        map.put("title", findTitle);
        map.put("article", ZhConverterUtil.toSimple(article));
        map.put("answer", service.searchForAnswer(question));
        map.put("related", relatedQuestions);
        return ResponseEntity.ok(map);
    }

    @GetMapping("/title")
    public ResponseEntity<List<PediaTitle>> listTitle() {
        return ResponseEntity.ok(pediaTitleService.listPediaTitle());
    }

    @PostMapping("/title")
    public ResponseEntity<PediaTitle> saveTitle(
            @RequestBody PediaTitle pediaTitle
    ) {
        PediaTitle savedPediaTitle = pediaTitleService.savePediaTitle(pediaTitle);
        return ResponseEntity.ok(savedPediaTitle);
    }

    @DeleteMapping("/title")
    public ResponseEntity<Void> deleteTitle(
            @RequestParam("title") String title
    ) {
        pediaTitleService.deletePediaTitle(title);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/wikititle")
    public ResponseEntity<String> findWikiTitle(
            @RequestParam("title") String title
    ) {
        return ResponseEntity.ok(HttpUtil.findTitle(title));
    }

}
