package com.qhieco.webservice;

import com.qhieco.request.web.StatisticsRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.StatisticsData;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/13 11:29
 * <p>
 * 类说明：
 * ${说明}
 */
public interface StatisticsService {

    /**
     * 根据条件获取统计数据
     * @param retuest
     */
    Resp findStatisticsByActivityType(StatisticsRequest retuest);

    /**
     * 获取活动触发统计记录
     * @param retuest
     * @return
     */
    Resp getActivityOffCountData(StatisticsRequest retuest);

    /**
     * 获取活动详情列表
     * @param retuest
     * @return
     */
    Resp getActivityCountByList(StatisticsRequest retuest);

    /**
     * 导出活动统计信息列表
     * @param retuest outputStream  cl
     * @return
     */
    Resp excel(StatisticsRequest retuest, OutputStream outputStream, Class cl)throws IOException;
}
