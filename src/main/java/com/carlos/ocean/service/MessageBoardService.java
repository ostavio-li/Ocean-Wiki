package com.carlos.ocean.service;

import com.carlos.ocean.mapper.MessageMapper;
import com.carlos.ocean.pojo.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/6/2
 */

@Service
public class MessageBoardService {

    @Autowired
    private MessageMapper messageMapper;

    public List<Message> list() {
        return messageMapper.selectAll();
    }

    public int deleteById(int id) {
        return messageMapper.deleteById(id);
    }

    public int save(Message message) {
        return messageMapper.insert(message);
    }

}
