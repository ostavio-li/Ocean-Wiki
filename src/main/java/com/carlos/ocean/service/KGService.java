package com.carlos.ocean.service;

import com.carlos.ocean.pojo.RelatedQuestion;
import com.carlos.util.http.HttpUtil;
import com.carlos.util.kg.KGUtil;
import com.carlos.util.nlp.NLPUtil;
import com.carlos.util.reg.RegUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * @author EdwardLee
 * @date 2021/4/4
 */

@Service
public class KGService {

    private static final int INTERRUPTED = 1;
    private static final int UNINTERRUPTED = 0;

    private Integer maxStackSize = 10;
    private Double similarity = 0.5;

    private final Deque<String> stack = new ArrayDeque<>(10);

    private volatile static int semaphore = UNINTERRUPTED;

    private RelatedQuestionService relatedQuestionService;
    private ArticleService articleService;

    @Autowired
    public void setRelatedQuestionService(RelatedQuestionService relatedQuestionService) {
        this.relatedQuestionService = relatedQuestionService;
    }

    @Autowired
    public void setArticleService(ArticleService articleService) {
        this.articleService = articleService;
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public boolean updateSimilarity(double similarity) {
        setSimilarity(similarity);
        System.out.println(getSimilarity());
        return true;
    }

    // TODO: 2021/4/20 搜索答案
    public String searchForAnswer(String question) {
        return KGUtil.getInstance().searchForAnswer(question);
    }

    public void clear() {
        semaphore = UNINTERRUPTED;
        KGUtil.getInstance().clear();
    }

    public void pauseBuild() {
        semaphore = INTERRUPTED;
    }

    public void build(String title, int level) throws InterruptedException {

        if (semaphore == INTERRUPTED) {
            throw new InterruptedException("暂停构建");
        }

        if (level < 1) {
            return;
        }

        if (this.stack.size() >= maxStackSize) {
            return;
        }

        System.out.println("Title: " + title);

        // 获取 文章
        String article = HttpUtil.findArticleByTitle(title);
        String findTitle = title;
        if (article == null || article.length() == 0) {
            findTitle = HttpUtil.findTitle(title);
            if (findTitle != null) {
                article = HttpUtil.findArticleByTitle(findTitle);
            } else {
                return;
            }

        }

        articleService.uploadArticle(findTitle, article);

        // 插入前，判断存在，防止递归内层重复插入节点！

        if (!KGUtil.getInstance().exists(findTitle)) {

            if (!this.stack.isEmpty()) {
                if (NLPUtil.checkSimilarity(this.stack.peekLast(), findTitle, this.similarity)) {
                    KGUtil.getInstance().addNode(findTitle);
                    this.stack.offerLast(findTitle);
                } else {
                    return;
                }
            } else {
                KGUtil.getInstance().addNode(findTitle);
                this.stack.offerLast(findTitle);
            }


        }

        // 分句
        List<String> sentences = RegUtil.splitSentences(article);

        for (String sentence : sentences) {
            if (sentence == null || sentence.length() == 0) {
                continue;
            }

            // 开头去重
            sentence = (title + sentence).replaceAll(title + title, title);

            // 转 简体
            sentence = ZhConverterUtil.toSimple(sentence);

            String[] parse = NLPUtil.parse(sentence);
            if (parse != null) {

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                for (int i = 0; i < parse.length; i++) {
//                    parse[i] = ZhConverterUtil.toSimple(parse[i]);
//                }



                if (KGUtil.getInstance().showNames().contains(parse[0])) {
                    KGUtil.getInstance().addExists(parse);
                } else {
                    KGUtil.getInstance().addRelation(parse);
                }
                String question = NLPUtil.generateQuestion(parse[0], parse[1]);
                if (question != null) {
                    relatedQuestionService.saveRelatedQuestion(
                            new RelatedQuestion(question,parse[0])
                    );
                }


//                if (!KGUtil.getInstance().existAbout(findTitle, parse[0])) {
//                    if (!findTitle.equals(parse[0])) {
//                        String[] temp = new String[3];
//                        temp[0] = findTitle;
//                        temp[1] = "有关";
//                        temp[2] = parse[0];
//                        KGUtil.getInstance().addLink(temp);
//                    }
//                }



                System.out.println("Sub: " + parse[0]);
                System.out.println("Verb: " + parse[1]);
                System.out.println("Obj: " + parse[2]);
                System.out.println();

//                String[] nextParse = NLPUtil.parse(parse[2]);
//                if (nextParse != null) {
//                    build(nextParse[0], level - 1);
//                }

                build(parse[2], level - 1);

            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        this.stack.pollLast();

    }

}
