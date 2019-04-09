package com.qhieco.mapper;

import com.qhieco.commonentity.Publish;
import com.qhieco.response.data.api.ParklocLockRespData;
import com.qhieco.response.data.api.ParklocPublishInfoRespData;
import com.qhieco.response.data.api.PublishParklocRespData;
import com.qhieco.response.data.api.PublishRespData;
import com.qhieco.time.ParklocShare;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/13 15:50
 * <p>
 * 类说明：
 * 有关车位的Mapper
 */
@Mapper
public interface ParklocMapper {

    List<PublishParklocRespData> queryAllParklocListByUserIdAdState(@Param("userId") Integer userId, @Param("state") Integer state,
                                                                    @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    List<PublishParklocRespData> queryPublishedParklocsByUserId(@Param("userId") Integer userId, @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    List<PublishRespData> queryPublishListByParklocIdAdState(@Param("parklocId") Integer parklocId, @Param("valid") Integer valid,
                                                             @Param("tobecancelled") int tobecancelled, @Param("tobealter") int tobealter);


    List<PublishParklocRespData> queryParklocPublishInfoByNumber(@Param("userId") Integer userId, @Param("parklocNumber") String parklocNumber,
                                                                 @Param("invalid") Integer valid, @Param("tobecancelled") Integer tobecancelled,
                                                                 @Param("tobealter") Integer tobealter);

    List<ParklocPublishInfoRespData> queryParklocPublishInfoByTime(@Param("userId") Integer userId, @Param("startTime") Long startTime,
                                                                   @Param("endTime") Long endTime, @Param("invalid") Integer valid,
                                                                   @Param("tobecancelled") Integer tobecancelled, @Param("tobealter") Integer tobealter,
                                                                   @Param("onceMode") Integer onceMode, @Param("loopMode") Integer loopMode,
                                                                   @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    List<ParklocShare> queryTimeoutShareList(@Param("sysCanceled") int sysCanceled,
                                             @Param("timeout") int timeout, @Param("userCanceled") int userCanceled,
                                             @Param("custServiceCanceled") int custServiceCanceled, @Param("invalid") int invalid);

    @Update(value = "UPDATE t_parkloc tpc set tpc.state=#{unpublished} WHERE tpc.id=#{parklocId}")
    void updateParklocStateByParklocId(@Param("parklocId") Integer parklocId, @Param("unpublished") Integer unpublished);

    List<ParklocLockRespData> queryParklocListByUserIdAndParklocNum(@Param("userId") Integer userId, @Param("parklocNum") String parklocNum, @Param("unpublished") int unpublished,
                                                                    @Param("startPage") int startPage, @Param("pageSize") int pageSize);

    /**
     * 查询一个车位下除失效之外所有的发布时间，还有模式为0时去掉当前时间>endTime的发布时间
     * @param parklocId 车位id
     */
    List<Publish> findValidPublishList(@Param("parklocId") Integer parklocId);
}
