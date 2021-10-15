package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.Article;
import com.carlos.ocean.service.ArticleService;
import com.carlos.ocean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Carlos Li
 * @date 2021/5/27
 */

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService service;

    @PostMapping("")
    public Result upload(
            @RequestBody Article article
    ) {
        service.uploadArticle(article.getTitle(), article.getContent());
        return Result.ok();
    }

    @GetMapping("")
    public Result getArticle(
            @RequestParam("title") String title
    ) {
        String content = service.getArticle(title);
        return content == null ? Result.error().message("未找到文章: " + title) : Result.ok().data("article", new Article(title, content));
    }

    @DeleteMapping("")
    public Result deleteArticle(
            @RequestParam("title") String title
    ) {
        service.deleteArticle(title);
        return Result.ok();
    }

    @GetMapping("/title")
    public Result getArticleTitles() {
        return Result.ok().data("titles", service.listArticleTitles());
    }

//    @PostMapping("/u")
//    public Result userUpload(
//            @RequestBody Article article
//    ) {
//
//    }

}
