package com.carlos.ocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.ocean.dto.CommentDTO;
import com.carlos.ocean.pojo.BmsComment;
import com.carlos.ocean.pojo.UmsUser;
import com.carlos.ocean.vo.CommentVO;


import java.util.List;


public interface IBmsCommentService extends IService<BmsComment> {
    /**
     *
     *
     * @param topicid
     * @return {@link BmsComment}
     */
    List<CommentVO> getCommentsByTopicID(String topicid);

    BmsComment create(CommentDTO dto, UmsUser principal);
}
