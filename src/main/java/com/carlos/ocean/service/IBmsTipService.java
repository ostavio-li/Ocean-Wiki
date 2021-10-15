package com.carlos.ocean.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.carlos.ocean.pojo.BmsTip;

public interface IBmsTipService extends IService<BmsTip> {
    BmsTip getRandomTip();
}
