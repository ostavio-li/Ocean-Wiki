package com.carlos.util.reg;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author EdwardLee
 * @date 2021/3/25
 */
public class RegUtil {

    static final String REG_REMOVE_HTML = "<[^>]+>";
    static final String REG_REMOVE_BRACKET = "[（|\\(].*[）|\\)]";
    static final String REG_REMOVE_NEWLINE = "(\\n){1,}";
    static final String REG_ALL = "(<[^>]+>)|((\\n){2,})|([（|\\(].*[）|\\)])";

//    static final String REG_REMOVE_OUTER_LINK = "(?<=外部链接).*";

//    public static void main(String[] args) throws IOException {
//
//        String wikiUri = "https://zh.wikipedia.org/w/api.php?action=query&titles=大不列顛島&format=json&prop=extracts&utf8";
//        String reg = "(<[^>]+>)|((\\n){2,})|([（|\\(].*[）|\\)])";
//        String regHtml = "<[^>]+>";
//
//
//
//
//        CloseableHttpClient client = HttpClientBuilder.create().build();
//        HttpGet get = new HttpGet(wikiUri);
//        // 配置 VPN Proxy
//        RequestConfig requestConfig = RequestConfig.custom().setProxy(new HttpHost("127.0.0.1", 1080)).build();
//        get.setConfig(requestConfig);
//
//        CloseableHttpResponse response = client.execute(get);
//        HttpEntity entity = response.getEntity();
//
//        String bodyStr = EntityUtils.toString(entity, StandardCharsets.UTF_8);
//
//        JSONObject body = JSONObject.parseObject(bodyStr);
//        JSONObject query = body.getJSONObject("query");
//        JSONObject pages = query.getJSONObject("pages");
//        String extract = pages.getJSONObject("86534").getString("extract");
//
//        String noHtml = simplifyByReg(REG_REMOVE_HTML, extract, "");
//        String res = simplifyByReg(REG_REMOVE_NEWLINE, noHtml, "\n");
//        res = simplifyByReg(REG_REMOVE_BRACKET, res, "");
////        res = simplifyByReg(REG_REMOVE_OUTER_LINK, res, "");
//
//
//
//        System.out.println(res);
//
//        System.out.println();
//
////        JSONObject.parseObject()
//
//
////        String target = "<p><b>温带海洋气候</b>（英文：Temperate marine climate）温带海洋性气候是一种全年温和潮湿的气候类型，其特征十分明显：冬无严寒，夏无酷暑，全年降水分配较为均匀。 分布在纬度40-60°之间的大陆西岸。\n</p>\n\n\n<h2><span id=\\\".E5.88.86.E5.B8.83\\\"></span><span id=\\\"分布\\\">分布</span></h2>\n<p>位于南北纬40至60度的大陆西岸，除亚洲、非洲和南极洲没有外，其余各大洲都有，其中以欧洲大陆西部及不列颠群岛最为典型。\\n</p>\\n<h2><span id=\\\".E6.88.90.E5.9B.A0\\\"></span><span id=\\\"成因\\\">成因</span></h2>\\n<p>常年盛行来自海洋的西风，西岸常有暖流影响，增温增湿，西风从暖洋面吹来，降水颇多。冬季常有锋面气旋来袭，因而尽管全年有雨，秋冬降雨量通常略多于春夏。雨以阵雨居多。\\n</p>\\n<h2><span id=\\\".E7.89.B9.E5.BE.81\\\"></span><span id=\\\"特征\\\">特征</span></h2>\\n<ol><li>冬暖夏凉，最冷月平均气温在0摄氏度以上，最热月平均气温在25摄氏度以下，年温差小。</li>\\n<li>全年湿润有雨，冬天雨水尤多。全年无干季，阴天多，晴天较少。</li></ol><h2><span id=\\\".E6.A4.8D.E8.A2.AB\\\"></span><span id=\\\"植被\\\">植被</span></h2>\\n<p>温带落叶阔叶林\\n</p>\\n<h2><span id=\\\".E5.86.9C.E4.B8.9A\\\"></span><span id=\\\"农业\\\">农业</span></h2>\\n<ul><li>粮食作物：不适合粮食作物</li>\\n<li>畜牧业：该气候不利于粮食作物及油料作物的生长，但利于多汁牧草生长。因此该气候带内居民饮食结构中肉类及乳制品占有较大比例，并成为西餐饮食文化的显著特色之一。</li></ul><h2><span id=\\\".E4.BB.A3.E8.A1.A8.E5.9F.8E.E5.B8.82\\\"></span><span id=\\\"代表城市\\\">";
//
////        String regRemoveN = "(\\n){2,}";
////        String regRemoveBracket = "[（|\\(].*[）|\\)]";
//
////        Pattern pattern = Pattern.compile(regHtml, Pattern.CASE_INSENSITIVE);
////        Matcher matcher = pattern.matcher(target);
////        String s = matcher.replaceAll("");
////        System.out.println("No HTML:");
////        System.out.println(s.trim());
////        System.out.println();
//
////        pattern = Pattern.compile(regRemoveN, Pattern.CASE_INSENSITIVE);
////        matcher = pattern.matcher(s);
////        String s1 = matcher.replaceAll(" ");
////        System.out.println("No n:");
////        System.out.println(s1.trim());
////        System.out.println();
////
////        pattern = Pattern.compile(regRemoveBracket, Pattern.CASE_INSENSITIVE);
////        matcher = pattern.matcher(s1);
////        String s2 = matcher.replaceAll(" ");
////        System.out.println("No ()");
////        System.out.println(s2.trim());
//    }

    public static String simplifyByReg(String regex, String target, String replace) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(target);
        return matcher.replaceAll(replace).trim();
    }

    public static String simplify(String text) {
        String noHtml = simplifyByReg(REG_REMOVE_HTML, text, "");
        String res = simplifyByReg(REG_REMOVE_NEWLINE, noHtml, "\n");
        res = simplifyByReg(REG_REMOVE_BRACKET, res, "");
        return res;
    }

    public static List<String> splitSentences(String text) {
        return Arrays.asList(text.split("\n|。|，|；"));
    }

}
