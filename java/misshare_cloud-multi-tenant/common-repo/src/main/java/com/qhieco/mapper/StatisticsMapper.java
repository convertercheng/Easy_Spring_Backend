package com.qhieco.mapper;

import com.qhieco.response.data.api.StatisticsData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/12 9:23
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface StatisticsMapper {

    void saveStatisticsData(StatisticsData.StatisticsBean item);

    Integer findActivityByTriggerType(@Param("userId") Integer userId, @Param("type") Integer types);

}

