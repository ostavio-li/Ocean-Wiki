package com.carlos.ocean.service;

import com.carlos.ocean.pojo.EditRecord;
import com.carlos.util.file.MinioUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author Carlos Li
 * @date 2021/5/27
 */

@Service
public class ArticleService {

    @Autowired
    private MinioUtil minioUtil;

    public boolean uploadArticle(String title, String content) {
        return minioUtil.uploadArticle(title, content);
    }

    public String getArticle(String title) {
        return minioUtil.getArticle(title);
    }

    public List<EditRecord> listEditRecords() {
        return minioUtil.listEditRecords();
    }

    // 获取全名
    public List<String> listArticleTitlesContains(String s) {
        return minioUtil.listArticleTitlesContains(s);
    }

    public List<String> listArticleTitlesEndsWith(String endsWith) {
        return minioUtil.listArticleTitlesEndsWith(endsWith);
    }

    public List<String> listArticleTitles() {
        return minioUtil.listArticleTitles();
    }

    public void deleteArticle(String title) {
        System.out.println("删除：" + title);
        minioUtil.deleteArticle(title);
    }

}
