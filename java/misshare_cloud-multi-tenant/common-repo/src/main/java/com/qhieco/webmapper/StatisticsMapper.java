package com.qhieco.webmapper;

import com.qhieco.request.web.StatisticsRequest;
import com.qhieco.response.data.web.StatisticsData;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/12 9:23
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface StatisticsMapper {

    /**
     * 根据条件获取统计数据
     * @param retuest
     * @return
     */
    List<StatisticsData> findStatisticsByActivityType(StatisticsRequest retuest);

    /**
     * 查询活动触发统计
     * @param retuest
     * @return
     */
    List<StatisticsData> getActivityOffCountData(StatisticsRequest retuest);

    /**
     * 查询活动统计信息列表
     * @param retuest
     * @return
     */
    List<StatisticsData> getActivityCountByList(StatisticsRequest retuest);

    /**
     * 查询活动统计总记录数
     * @param retuest 相对的参数条件
     * @return
     */
    Integer pageActivityCountTotalCount(StatisticsRequest retuest);
    void insertStatistics(StatisticsRequest retuest);

}

