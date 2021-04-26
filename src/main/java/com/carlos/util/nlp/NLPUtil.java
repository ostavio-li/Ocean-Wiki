package com.carlos.util.nlp;

import com.baidu.aip.nlp.AipNlp;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @author EdwardLee
 * @date 2021/3/25
 */
public class NLPUtil {

    public static final String APP_ID = "23854166";
    public static final String API_KEY = "HwLGYdj8niDLwf8CmDtG2EQ2";
    public static final String SECRET_KEY = "EkterFsCYGQpZYkMmc1KYnvcpqOvU4Ld";

    private static final int CONNECTION_TIMEOUT_MS = 2000;
    private static final int SOCKET_TIMEOUT_MS = 6000;

    private static final AipNlp client = new AipNlp(APP_ID, API_KEY, SECRET_KEY);

    static {
        client.setConnectionTimeoutInMillis(CONNECTION_TIMEOUT_MS);
        client.setSocketTimeoutInMillis(SOCKET_TIMEOUT_MS);
    }

    public static String[] parse(String target) {

        // **********************

        int objId = -1;
        int subId = -1;
        int verbId = -1;

        StringBuilder sub = new StringBuilder();
        StringBuilder verb = new StringBuilder();
        StringBuilder object = new StringBuilder();

        // **********************

        JSONObject result = client.depParser(target, null);
        System.out.println(result.toString(2));

        JSONArray items = result.getJSONArray("items");
//        System.out.println(items.toString(2));

        // 遍历 items
        OUT:
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);

            // 如果 item 是 主谓关系，且为主语
            if ("SBV".equals(item.getString("deprel")) && item.getString("postag").contains("n")) {
                subId = item.getInt("id");

                sub.append(item.getString("word"));
                int currId = subId;
                currId--;
                while (currId >= 1) {
                    JSONObject currObject = getObjectById(items, currId);
                    String deprel = currObject.getString("deprel");

                    if (
                            "ATT".equals(deprel)
                            || "DE".equals(deprel)
                    ) {
                        sub.insert(0, currObject.getString("word"));
                    }
                    currId--;
                }
//                System.out.println("Sub: " + sub.toString());

                // 获取 谓语动词
                JSONObject v = getObjectById(items, item.getInt("head"));
//                System.out.println("v.postag: " + v.getString("postag"));
                if ("v".equals(v.getString("postag"))) {
                    verbId = v.getInt("id");
//                    System.out.println("verb id: " + verbId);

                    currId = subId;
                    while (currId < verbId) {
                        JSONObject currObject = getObjectById(items, currId);
                        String deprel = currObject.getString("deprel");
                        if ("ATT".equals(deprel) || "ADV".equals(deprel)) {
                            verb.append(currObject.getString("word"));
                        }
                        currId++;
                    }
                    verb.append(v.getString("word"));
//                    System.out.println("Verb: " + verb.toString());

                    // 扫描 宾语
                    for (int j = items.length() - 1; j > verbId; j--) {
                        JSONObject obj = items.getJSONObject(j);
                        // 如果是 动宾结构
                        if (
                                "VOB".equals(obj.getString("deprel"))
                                || "POB".equals(obj.getString("deprel"))
                                || "COO".equals(obj.getString("deprel"))
                        ) {
                            objId = obj.getInt("id");
                            currId = verbId;
                            while (currId < objId) {
                                JSONObject currObject = getObjectById(items, currId);
                                String deprel = currObject.getString("deprel");
                                if (
                                        "ATT".equals(deprel)
                                        || "DE".equals(deprel)
                                        || "QUN".equals(deprel)
                                        || "YGC".equals(deprel)
                                        || "TMP".equals(deprel)
                                        || "APP".equals(deprel)
                                        || "LOC".equals(deprel)
                                        || "POB".equals(deprel)
                                        || "VOB".equals(deprel)
                                        || "COO".equals(deprel)
                                        || "TOP".equals(deprel)
                                        || "WP".equals(deprel)
                                        || "ADV".equals(deprel)
                                        || "SBV".equals(deprel)
                                ) {
                                    object.append(currObject.getString("word"));
                                }
                                currId++;
                            }
                            object.append(obj.getString("word"));
                            break OUT;
                        }



                    }
                }
            }
        }
        if (sub.length() > 0 && verb.length() > 0 && object.length() > 0) {
            String[] parsedArray = new String[3];
            parsedArray[0] = sub.toString();
            parsedArray[1] = verb.toString();
            parsedArray[2] = object.toString();
            return parsedArray;
        } else {
            return null;
        }
    }

    /**
     * 解析问句，获得三元组
     * @param question 问句
     */
    public static String[] parseQuestion(String question) {
        HashMap<String, Object> options = new HashMap<String, Object>();
        options.put("mode", 1);
        JSONArray result = client.depParser(question, options).getJSONArray("items");
        System.out.println(result.toString(2) + "\n");
        if (result.length() == 3) {
            String[] parsedArray = new String[3];
            for (int i = 0; i < result.length(); i++) {
                parsedArray[i] = result.getJSONObject(i).getString("word");
                System.out.println(parsedArray[i]);
            }
            return parsedArray;
        }
        for (int i = 0; i < result.length(); i++) {
            JSONObject item = result.getJSONObject(i);
            if ("SBV".equals(item.getString("deprel")) && item.getString("postag").contains("n")) {

            }
        }
        return null;
    }

    public static JSONObject getObjectById(JSONArray array, int id) {
        if (id == -1) {
            return null;
        }
        JSONObject obj;
        for (int i = 0; i < array.length(); i++) {
            obj = array.getJSONObject(i);
            if (obj.getInt("id") == id) {
                return obj;
            }
        }
        return null;
    }

    public static JSONObject getBy(JSONArray array, String factorProp, String factor) {
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            if (factor.equals(jsonObject.getString(factorProp))) {
                return jsonObject;
            }
        }
        return null;
    }

    public static boolean checkSimilarity(String text1, String text2, double similarity) {
        if (text1 == null || text2 == null) {
            return false;
        }
        JSONObject res = client.simnet(text1, text2, null);
        if (res.has("score")) {
            double score = res.getDouble("score");
            System.out.println("Text1: " + text1);
            System.out.println("Text 2: " + text2);
            System.out.println("相似度: " + score);
            return score >= similarity;
        } else {
            return false;
        }
    }

    public static void main(String[] args) {
        parseQuestion("大西洋取自哪里");
    }

}