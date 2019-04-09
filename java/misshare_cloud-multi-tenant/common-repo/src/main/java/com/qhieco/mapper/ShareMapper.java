package com.qhieco.mapper;

import com.qhieco.commonentity.Share;
import com.qhieco.response.data.api.ParklocShareRepData;
import com.qhieco.response.data.api.ReserveTimeData;
import com.qhieco.time.ParklocShare;
import com.qhieco.time.ShareTimeOutInfo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/8 16:52
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ShareMapper {
    /**
     * 查询小区可预约车位时间列表
     *
     * @param params
     * @return
     */
    List<ReserveTimeData> queryReserveTimeListByCondition(HashMap<String, Object> params);

    List<ReserveTimeData> queryReserveTimeListByParklocIdAndCondition(HashMap<String, Object> params);

    List<ReserveTimeData> queryReserveTimeListByDistrictId(HashMap<String, Object> params);


    /**
     * 查询小区所有的可预约时间段列表，返回字段包含车位编号，车位id等
     *
     * @param params
     * @return
     */
    List<ParklocShareRepData> queryReserveTimeListByParklotId(HashMap<String, Object> params);

    @Update(value = "update t_share set state=#{state} where id=#{shareId}")
    void updateShareStateById(@Param("shareId") int shareId, @Param("state") int state);

    void updateBatchShareStateByIds(@Param("parklocShareList") List<ParklocShare> parklocShareList, @Param("state") int state);

    @Delete(value = "delete from t_share where id=#{shareId}")
    void deleteShareById(@Param("shareId") int shareId);

    void insertBatch(@Param("shareList") List<Share> shareList);

    /**
     * 查询当前时间可预约的车位
     *
     * @return 可预约的数量
     */
    Long countParklocReservable();

    List<ShareTimeOutInfo> queryShareTimeOutList(@Param("timeInterval") int timeInterval);
}
