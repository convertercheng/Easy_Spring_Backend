package com.qhieco.webmapper;

import com.qhieco.request.web.ActivityQuery;
import com.qhieco.request.web.ActivitySameTimeQuery;
import com.qhieco.response.data.web.ActivityDetailData;
import com.qhieco.response.data.web.ActivityInfoData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:48
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ActivityMapper {

    public List<ActivityInfoData> queryActivityList(ActivityQuery request);

    int findSameTimeActivity(ActivitySameTimeQuery request);

    public int queryCountActivityList(ActivityQuery request);

    public ActivityDetailData queryActivityDetailInfoById(Integer id);

    @Update(value = "update t_activity set state = #{state} where id=#{id}")
    public void updateActivityState(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 查询与奖品绑定的有效活动数(状态为有效的 并且 活动有效期尚未过期)
     *
     * @param prizeId
     * @return
     */
    public int countValidActivityPrizeByPrizeId(@Param("prizeId") Integer prizeId);
}
