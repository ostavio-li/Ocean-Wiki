package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.PediaTitle;
import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.ocean.service.ArticleService;
import com.carlos.ocean.service.KGService;
import com.carlos.ocean.service.PediaTitleService;
import com.carlos.ocean.service.RelatedQuestionService;
import com.carlos.ocean.vo.Result;
import com.carlos.ocean.vo.ResultCode;
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
    private ArticleService articleService;

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

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }


    @DeleteMapping("")
    public Result clear() {
        service.clear();
        pediaTitleService.clearAll();
        return Result.ok();
    }

    @GetMapping("")
    public Result buildKg(
            @RequestParam("title") String kgTitle,
            @RequestParam("recurr") int recurrence
    ) {
        logger.info("Recurrence: " + recurrence);
        System.out.println("KGTitle: " + kgTitle);
        try {
            service.build(kgTitle, recurrence);
        } catch (InterruptedException e) {
            return Result.ok();
        }
        return Result.ok();
    }

    @GetMapping("/pause")
    public Result pauseBuild() {
        service.pauseBuild();
        return Result.ok();
    }

    @PutMapping("/sim")
    public Result updateSimilarity(
            @RequestParam("similarity") double similarity
    ) {
        if (service.updateSimilarity(similarity)) {
            return Result.ok();
        } else {
            return Result.error();
        }
    }

    @GetMapping("/sim")
    public ResponseEntity<Double> getSimilarity() {
        return ResponseEntity.ok(service.getSimilarity());
    }

    @GetMapping("/search")
    public Result searchForAnswer(
            @RequestParam("question") String question
    ) {
        String[] parsed = NLPUtil.parseQuestion(question);
        String title = parsed[0];
        String findTitle = title;
        // 从系统获取文章
        String article = articleService.getArticle(title);

        if (article == null) {

            System.out.println("开始爬取文章");
            // 用 title 爬取文章
            article = HttpUtil.findArticleByTitle(title);

            if (article == null || article.length() == 0) {
                findTitle = HttpUtil.findTitle(title);
                if (findTitle != null) {
                    article = HttpUtil.findArticleByTitle(findTitle);
                } else {
                    article = "";
                }
            }
        }

        Map<String, Object> map = new HashMap<>(4);

        if (parsed.length == 1) {
            map.put("title", findTitle);
            map.put("article", ZhConverterUtil.toSimple(article));
            map.put("answer", "见下文");
            map.put("related", relatedQuestionService.listAllByTitleWithoutQuestion(findTitle, ""));
            return Result.ok().data(map);
        }


        List<RelatedQuestion> relatedQuestions = relatedQuestionService.listAllByTitleWithoutQuestion(findTitle, question);

        map.put("title", findTitle);
        map.put("article", ZhConverterUtil.toSimple(article));
        map.put("answer", service.searchForAnswer(question));
        map.put("related", relatedQuestions);
        return Result.ok().data(map);
    }

    @GetMapping("/title")
    public Result listTitle() {
        return Result.ok().data("titles", pediaTitleService.listPediaTitle());
    }

    @PostMapping("/title")
    public Result saveTitle(
            @RequestBody PediaTitle pediaTitle
    ) {
        PediaTitle savedPediaTitle = pediaTitleService.savePediaTitle(pediaTitle);
        return Result.ok().data("title", savedPediaTitle);
    }

    @DeleteMapping("/title")
    public Result deleteTitle(
            @RequestParam("title") String title
    ) {
        pediaTitleService.deletePediaTitle(title);
        return Result.ok();
    }

//    @GetMapping("/wikititle")
//    public Result findWikiTitle(
//            @RequestParam("title") String title
//    ) {
//        return ResponseEntity.ok(HttpUtil.findTitle(title));
//    }

}