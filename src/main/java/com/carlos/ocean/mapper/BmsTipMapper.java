package com.carlos.ocean.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.carlos.ocean.pojo.BmsTip;
import org.springframework.stereotype.Repository;

@Repository
public interface BmsTipMapper extends BaseMapper<BmsTip> {
    BmsTip getRandomTip();
}