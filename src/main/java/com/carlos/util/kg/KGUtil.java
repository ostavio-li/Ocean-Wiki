package com.carlos.util.kg;

import com.carlos.util.http.HttpUtil;
import com.carlos.util.nlp.NLPUtil;
import com.carlos.util.reg.RegUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.neo4j.driver.*;

import java.util.*;

/**
 * @author EdwardLee
 * @date 2021/3/30
 */
public class KGUtil implements AutoCloseable {

    private static String uri = "bolt://localhost:7687";
    private static String userName = "neo4j";
    private static String password = "carlos";

    private final Driver driver;
    private final Deque<String> stack;

    private Double similarity;
    private Integer maxStackSize;

    // Singleton
    private static final KGUtil instance = new KGUtil(uri, userName, password);

    private KGUtil(String uri, String username, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
        this.stack = new ArrayDeque<>(10);
        this.similarity = 0.5;
        this.maxStackSize = 10;
    }

    public static KGUtil getInstance() {
        return instance;
    }

    public List<String> showNames() {
        try (Session session = driver.session()) {
//            String from = session.readTransaction(transaction -> {
            Result result = session.run("match(a:Sub) return a.name");
            List<String> names = new ArrayList<>();

            while (result.hasNext()) {
                Record record = result.next();
                Map<String, Object> map = record.asMap();
                Set<Map.Entry<String, Object>> keys = map.entrySet();
                for (Map.Entry<String, Object> key : keys) {
                    names.add(key.getValue().toString());
                }
            }
            return names;


        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void addRelation(String[] relation) {
        System.out.println("插入 实体关系 ...");


        try (Session session = driver.session()) {
            session.run("create (a:Sub {name:'" + relation[0] +  "'}), (b:Obj {name:'" + relation[2] + "'}), (a) - [:" + relation[1] + "] -> (b) return a.name");
            System.out.println("插入完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void addExists(String[] relation) {
        System.out.println("插入 实体关系 ...");
        String cypher = "match(a:Sub {name:'" + relation[0] + "'}) create(b:Obj {name:'" + relation[2] + "'}), (a) - [:" + relation[1] + "] -> (b) return a.name";

        try (Session session = driver.session()) {
            session.run(cypher);
            System.out.println("插入完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addNode(String name) {
        System.out.println("插入 实体 ...");
        String cypher = "create(a:Sub {name:'" + name + "'}) return a.name";

        try (Session session = driver.session()) {
            session.run(cypher);
            System.out.println("插入完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addLink(String[] relation) {
        System.out.println("插入 链接 ...");
        String cypher = "match(a:Sub {name:'" + relation[0] + "'}), (b:Sub {name:'" + relation[2] + "'}) create (a) - [:" + relation[1] + "] -> (b) return a.name";

        try (Session session = driver.session()) {
            session.run(cypher);
            System.out.println("插入完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clear() {
        String cypher = "match(a) detach delete a";

        try (Session session = driver.session()) {
            session.run(cypher);
            System.out.println("清空完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void build(String title, int level) {

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

        // 插入前，判断存在，防止递归内层重复插入节点！

        if (!exists(findTitle)) {

            if (!this.stack.isEmpty()) {
                if (NLPUtil.checkSimilarity(this.stack.peekLast(), findTitle, this.similarity)) {
                    addNode(findTitle);
                    this.stack.offerLast(findTitle);
                } else {
                    return;
                }
            } else {
                addNode(findTitle);
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
                    Thread.sleep(2000);
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

    @Override
    public void close() throws Exception {
        this.driver.close();
    }

    public boolean existAbout(String findTitle, String s) {
        String cypher = "match(a:Sub {name:'" + findTitle + "'}) - [r:有关] -> (b:Sub {name:'" + s + "'}) return r";

        try (Session session = driver.session()) {
            Result r = session.run(cypher);
            return r.hasNext();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean exists(String name) {
        String cypher = "match(a:Sub {name:'" + name + "'}) return a";
        try (Session session = driver.session()) {
            Result r = session.run(cypher);
            return r.hasNext();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void setSimilarity(Double similarity) {
        this.similarity = similarity;
    }

    public Double getSimilarity() {
        return similarity;
    }

    public String searchForAnswer(String question) {

        // 模糊查询模板
        String cypherFormat = "match(a:Sub) - [r] -> (b:Obj) where a.name=~'.*%s.*' and type(r)=~'.*%s.*' return b.name";
//
        String[] items = NLPUtil.parseQuestion(ZhConverterUtil.toSimple(question));


//        String subject = items[0];
//        String predict = items[1];
//        try (Session session = driver.session()) {
//            Result result = session.run(String.format(cypherFormat, items));
//            while (result.hasNext()) {
//                Record record = result.next();
//
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return null;
//        }


        String cypher = String.format(cypherFormat, items[0], items[1]);
        System.out.println(cypher);

        String res = null;
        try (Session session = driver.session()) {
            Result result = session.run(cypher);
            if (result.hasNext()) {
                Record next = result.next();
                res = (String) next.asMap().get("b.name");
                System.out.println("结果：" + res);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return res != null ? res : "抱歉，未找到结果";
    }

    public static void main(String[] args) {
        KGUtil.getInstance().searchForAnswer("大西洋从赤道分为什么");
    }

}