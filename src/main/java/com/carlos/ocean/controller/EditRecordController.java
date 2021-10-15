package com.carlos.ocean.controller;

import com.carlos.ocean.pojo.EditRecord;
import com.carlos.ocean.service.ArticleService;
import com.carlos.ocean.service.EditRecordService;
import com.carlos.ocean.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Carlos Li
 * @date 2021/5/28
 */

@RestController
@RequestMapping("/editrecord")
public class EditRecordController {

    @Autowired
    private EditRecordService service;

    @Autowired
    private ArticleService articleService;

    @GetMapping("")
    public Result list() {
        List<EditRecord> editRecords = service.listRecords();
        return Result.ok().data("records", editRecords);
    }

    @GetMapping("/diff")
    public Result diff(
            @RequestParam("title") String title
    ) {
        String rawTitle = title.split("_")[1];
        String newArticle = articleService.getArticle(title);
        String oldArticle = articleService.getArticle(rawTitle);
        Map<String, Object> map = new HashMap<>(2);
        map.put("oldArticle", oldArticle);
        map.put("newArticle", newArticle);
        return Result.ok().data(map);
    }

    // TODO: 2021/5/29 接受编辑
    @PostMapping("/accept")
    public Result acceptEdit(
            @RequestBody EditRecord editRecord
    ) {

        // 将此提交合并
        articleService.uploadArticle(editRecord.getTitle(), articleService.getArticle(editRecord.getUsername() + "_" + editRecord.getTitle()));

        // 删除相关提交记录
        List<String> titles = articleService.listArticleTitlesEndsWith("_" + editRecord.getTitle());
        for (String title : titles) {
            articleService.deleteArticle(title);
        }


        return Result.ok();
    }

}
