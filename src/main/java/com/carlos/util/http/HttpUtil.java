package com.carlos.util.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.carlos.util.reg.RegUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;

/**
 * @author EdwardLee
 * @date 2021/3/28
 */
public class HttpUtil {

    static final String URI_WIKI_ARTICLE = "https://zh.wikipedia.org/w/api.php?action=query&titles=$$$$&format=json&prop=extracts&utf8";
    static final String URI_WIKI_LIST = "https://zh.wikipedia.org/w/api.php?action=query&list=search&srsearch=$$$$&rawcontinue=1&format=json&utf8";

    public static String findArticleByTitle(String title) {
        String uri = URI_WIKI_ARTICLE.replace("$$$$", title);

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet(uri);
        RequestConfig requestConfig = RequestConfig.custom().setProxy(new HttpHost("127.0.0.1", 1080)).build();
        get.setConfig(requestConfig);

        CloseableHttpResponse response = null;

        try {

            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            String bodyStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            JSONObject body = JSONObject.parseObject(bodyStr);
            JSONObject query = body.getJSONObject("query");
            JSONObject pages = query.getJSONObject("pages");

            Set<Map.Entry<String, Object>> entries = pages.entrySet();

            String[] pageId = new String[1];

            entries.forEach(elem -> pageId[0] = elem.getKey());


            String extract = pages.getJSONObject(pageId[0]).getString("extract");

            return extract != null ? RegUtil.simplify(extract) : null;


        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (client != null) {
                    client.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public static String findTitle(String title) {
        String uri = URI_WIKI_LIST.replace("$$$$", title);

        CloseableHttpClient client = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet(uri);
        RequestConfig requestConfig = RequestConfig.custom().setProxy(new HttpHost("127.0.0.1", 1080)).build();
        get.setConfig(requestConfig);

        CloseableHttpResponse response = null;

        try {

            response = client.execute(get);
            HttpEntity entity = response.getEntity();
            String bodyStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
            JSONObject body = JSONObject.parseObject(bodyStr);
            JSONObject query = body.getJSONObject("query");
            JSONArray search = query.getJSONArray("search");
            JSONObject item = search.getJSONObject(0);
            return item.getString("title");

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {

            try {
                if (client != null) {
                    client.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}