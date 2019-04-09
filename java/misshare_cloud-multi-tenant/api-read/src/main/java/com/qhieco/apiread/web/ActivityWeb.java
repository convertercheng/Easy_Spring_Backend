package com.qhieco.apiread.web;

import com.qhieco.apiservice.ActivityService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ActivityListRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ActivityRespData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 11:46
 * <p>
 * 类说明：
 * 活动controller
 */
@RestController
@RequestMapping(value = "activity")
@Slf4j
public class ActivityWeb {
    @Autowired
    private ActivityService activityService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    /**
     * 活动列表，包括首页banner显示和活动列表显示
     *
     * @param request
     * @return
     */
    @PostMapping(value = "list")
    public Resp activityList(@RequestBody ActivityListRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getPage_num() == null || request.getPage_num() < 0 || request.getState() == null) {

        }
        List<ActivityRespData> activityRespDataList = activityService.queryActivityList(request.getPage_num(), request.getState());

        for (ActivityRespData activityRespData : activityRespDataList) {
            if (!StringUtils.isEmpty(activityRespData.getFilePath())) {
                activityRespData.setFilePath(configurationFiles.getPicPath() + activityRespData.getFilePath());
            }
        }
        return RespUtil.successResp(activityRespDataList);
    }
}

