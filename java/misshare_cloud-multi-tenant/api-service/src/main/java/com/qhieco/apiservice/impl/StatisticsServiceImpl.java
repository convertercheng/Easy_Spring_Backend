package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.StatisticsService;
import com.qhieco.mapper.StatisticsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/17 16:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    StatisticsMapper statisticsMapper;

}
