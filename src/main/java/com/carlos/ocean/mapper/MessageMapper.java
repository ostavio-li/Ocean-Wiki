package com.carlos.ocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.ocean.pojo.Message;

import java.util.List;

/**
 * @author Carlos Li
 * @date 2021/6/2
 */
public interface MessageMapper extends BaseMapper<Message> {
    List<Message> selectAll();
}
