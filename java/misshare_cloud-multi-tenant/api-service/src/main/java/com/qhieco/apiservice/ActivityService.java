package com.qhieco.apiservice;

import com.qhieco.response.data.api.ActivityRespData;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 12:32
 * <p>
 * 类说明：
 * ${说明}
 */
public interface ActivityService {

    /**
     * 查询活动列表
     * @param pageNum
     * @param state
     * @return
     */
    List<ActivityRespData> queryActivityList(Integer pageNum, Integer state);

    List<ActivityRespData> findActivityTriggerTypeById(Integer activityId);

    /**
     * 根据活动类型获取活动
     * @param state
     * @return
     */
    public List<ActivityRespData> findActivityByType(Integer state);

}
