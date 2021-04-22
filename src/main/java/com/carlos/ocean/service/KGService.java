package com.carlos.ocean.service;

import com.carlos.util.kg.KGUtil;
import org.springframework.stereotype.Service;

/**
 * @author EdwardLee
 * @date 2021/4/4
 */

@Service
public class KGService {

    public boolean updateSimilarity(double similarity) {
        KGUtil.getInstance().setSimilarity(similarity);
        System.out.println(KGUtil.getInstance().getSimilarity());
        return true;
    }

    public double getSimilarity() {
        return KGUtil.getInstance().getSimilarity();
    }

    // TODO: 2021/4/20 搜索答案
    public String searchForAnswer(String question) {
        return "意大利传教士利玛窦撰写的《山海舆地全图》";
    }

}
