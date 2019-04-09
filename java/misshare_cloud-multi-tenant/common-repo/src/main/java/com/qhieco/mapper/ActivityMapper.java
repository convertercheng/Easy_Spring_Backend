package com.qhieco.mapper;

import com.qhieco.response.data.api.ActivityRespData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/4 12:31
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ActivityMapper {

    /**
     * 查询活动列表
     *
     * @param now               当前时间戳
     * @param activityFileState 1：长图 首页显示， 2：宽图 列表显示
     * @param startPage         分页，开始条数
     * @param pageSize          分页， 每页条数
     * @return
     */
    List<ActivityRespData> queryActivityList(@Param("now") long now, @Param("activityFileState") Integer activityFileState,
                                             @Param("startPage") Integer startPage, @Param("pageSize") Integer pageSize);

    /**
     * 查询已过期的活动
     *
     * @return
     */
    @Select(value = "SELECT tac.id FROM t_activity tac WHERE tac.`end_time`<=UNIX_TIMESTAMP(NOW()) * 1000 AND tac.`state` =1")
    List<Integer> queryTimeOutActivityList();

    /**
     * 通过id更新活动的状态
     *
     * @param id
     */
    @Update(value = "UPDATE t_activity SET state=#{state} WHERE id=#{id}")
    void updateState(@Param("id") Integer id, @Param("state") Integer state);

    /**
     * 根据活动类型获取活动详细
     * @param type
     * @return
     */
    List<ActivityRespData> findActivityByType(Integer type);

    ActivityRespData findActivityByTriggerType(Integer userId, String types);

    List<ActivityRespData> findActivityTriggerTypeById(Integer activityId);
}
