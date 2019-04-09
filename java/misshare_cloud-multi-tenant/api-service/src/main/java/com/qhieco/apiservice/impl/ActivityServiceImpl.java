package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ActivityService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.ActivityMapper;
import com.qhieco.response.data.api.ActivityRespData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 12:32
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;

    @Override
    public List<ActivityRespData> queryActivityList(Integer pageNum, Integer state) {
        Integer startPage = pageNum * Constants.PAGE_SIZE;
        Long now = System.currentTimeMillis();
        return activityMapper.queryActivityList(now, state, startPage, Constants.PAGE_SIZE);
    }

    @Override
    public List<ActivityRespData> findActivityTriggerTypeById(Integer activityId) {
        return activityMapper.findActivityTriggerTypeById(activityId);
    }

    /**
     * 根据活动类型获取活动详细
     * @param type
     * @return
     */
    @Override
    public List<ActivityRespData> findActivityByType(Integer type) {
        return activityMapper.findActivityByType(type);
    }

}
