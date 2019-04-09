package com.qhieco.mapper;

import com.qhieco.response.data.api.ParklotIdAdParklocIdData;
import com.qhieco.time.ParklotIdAdParklocIds;
import com.qhieco.time.ValidPublishInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/26 17:06
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface PublishMapper {
    public void updatePublishOnceTimeout(@Param("valid") int valid, @Param("invalid") int invalid, @Param("once") int once, @Param("now") long now);

    public List<ValidPublishInfo> queryValidPublishList(@Param("loop") int loop, @Param("valid") int valid, @Param("invalid") int invalid);

    public void updateParklocState(HashMap params);

    public List<ParklotIdAdParklocIds> queryNoPublishedStateParklots(HashMap params);

    public List<Integer> queryReservateTimeList(@Param("advanceReservationTimeKey") String advanceReservationTimeKey,
                                                @Param("timeInterval") Integer timeInterval);

    public ParklotIdAdParklocIdData queryParklocIdAdParklotIdByPublishId(@Param("publishId") Integer publishId);
}
