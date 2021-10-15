package com.carlos.util.kg;

import com.carlos.ocean.pojo.Trib;
import com.carlos.ocean.service.ArticleService;
import com.carlos.ocean.service.RelatedQuestionService;
import com.carlos.util.http.HttpUtil;
import com.carlos.util.nlp.NLPUtil;
import com.carlos.util.reg.RegUtil;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.neo4j.driver.*;
import org.neo4j.driver.types.Node;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author EdwardLee
 * @date 2021/3/30
 */

// match(a:Sub)-[r]->(b:Obj) where not(a.name in ['大西洋中洋脊间普遍水深', '大西洋分界'])  return a,r,b
// match(a) detach delete a

public class KGUtil implements AutoCloseable {

    private static String uri = "bolt://localhost:7687";
    private static String userName = "neo4j";
    private static String password = "carlos";


    private final Driver driver;

    // Singleton
    private static final KGUtil instance = new KGUtil(uri, userName, password);

    private KGUtil(String uri, String username, String password) {
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
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
        String cypher = "match(a:Sub)-[r]->(b:Obj) where not(a.name in ['印度洋'])  delete a,r,b";
        try (Session session = driver.session()) {
            session.run(cypher);
            System.out.println("清空完成!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Trib> visualize(String sub, int n) {
        String cypher = "match(a:Sub)-[r]-(b:Obj) where a.name='" + sub + "' return a,r,b limit " + n;
        try (Session session = driver.session()) {
            Result result = session.run(cypher);
            List<Trib> list = new ArrayList<>();
            while (result.hasNext()) {
                Record next = result.next();
                Map<String, Object> map = next.asMap();
                Node a = (Node) map.get("a");
                Relationship r = (Relationship) map.get("r");
                Node b = (Node) map.get("b");

                Trib trib = new Trib(
                        a.get("name").asString(),
                        r.type(),
                        b.get("name").asString()
                );

                list.add(trib);

            }
            return list;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
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


    public String searchForAnswer(String question) {

        // 模糊查询模板
        String cypherFormat = "match(a:Sub) - [r] -> (b:Obj) where a.name=~'.*%s.*' and type(r)=~'.*%s.*' return b.name";

        String[] items = NLPUtil.parseQuestion(ZhConverterUtil.toSimple(question));

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

}