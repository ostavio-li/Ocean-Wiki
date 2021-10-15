package com.carlos.ocean.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.carlos.ocean.mapper.BmsBillboardMapper;
import com.carlos.ocean.pojo.BmsBillboard;
import com.carlos.ocean.service.IBmsBillboardService;
import org.springframework.stereotype.Service;

@Service
public class IBmsBillboardServiceImpl extends ServiceImpl<BmsBillboardMapper, BmsBillboard> implements IBmsBillboardService {

}
