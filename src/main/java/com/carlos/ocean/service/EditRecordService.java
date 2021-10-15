package com.carlos.ocean.service;

import com.carlos.ocean.pojo.EditRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/5/28
 */
@Service
public class EditRecordService {

    @Autowired
    private ArticleService articleService;

    public List<EditRecord> listRecords() {
        return articleService.listEditRecords();
    }

}
