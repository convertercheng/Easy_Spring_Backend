package com.qhieco.webservice;

import com.qhieco.request.web.FeekbackRequest;
import com.qhieco.request.web.LogOperationWebRequest;
import com.qhieco.request.web.LoginLogRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 10:56
 * <p>
 * 类说明：
 * ${说明}
 */
public interface SysManageService {

    public AbstractPaged<FeedBackData> queryFeekbackList(String phone, Long startTime, Long endTime, List<Integer> proIdList, Integer startPage, Integer pageSize, Integer sEcho);

    /**
     * 用户反馈列表导出
     * @param feekbackRequest
     * @return
     */
    public Resp queryFeedbackListByConditionExcel(FeekbackRequest feekbackRequest, OutputStream outputStream)throws IOException;

    public AbstractPaged<LogOperationData> queryLogOperateList(Integer startPage, Integer pageSize, Integer sEcho);

    public AbstractPaged<LogLoginData> queryLogLoginList(String phone, String phoneModel, Long loginStartTime, Long loginEndTime,
                                                         Integer startPage, Integer pageSize, Integer sEcho);

    /**
     * APP用户导出
     * @param loginLogRequest
     * @return
     */
    public Resp queryLogLoginExcel(LoginLogRequest loginLogRequest, OutputStream outputStream)throws IOException;
    /**
     * 查询管理员日志列表
     * @param logOperationWebRequest
     * @return
     */
    public AbstractPaged<LogOperationWebData> queryOperationWebList(LogOperationWebRequest logOperationWebRequest);

    /**
     * 管理员日志列表Excel导出
     * @param logOperationWebRequest
     * @return
     */
    public Resp operationWebListExcel(LogOperationWebRequest logOperationWebRequest, OutputStream outputStream)throws IOException;

}
