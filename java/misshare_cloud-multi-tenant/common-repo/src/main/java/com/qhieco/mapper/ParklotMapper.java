package com.qhieco.mapper;

import com.qhieco.commonentity.FeeRuleParking;
import com.qhieco.commonentity.FeeRuleReserve;
import com.qhieco.commonentity.LogLock;
import com.qhieco.commonentity.Parklot;
import com.qhieco.response.data.api.ParkingFeeData;
import com.qhieco.response.data.api.ParklotQueryRepData;
import com.qhieco.response.data.api.ParklotUsualRepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 14:00
 * <p>
 * 类说明：
 * 停车场mapper
 */
@Mapper
public interface ParklotMapper {

    ParklotUsualRepData queryParklotUsualByUserId(Integer userId);

    List<ParklotQueryRepData> queryParklotListByKeywords(HashMap<String, Object> params);


    /**
     * 查询车场的信息
     * @param mapLatitude 传来的地图中心的纬度
     * @param mapLongitude 传来的地图中心的经度
     * @param locateLatitude 传来的定位纬度
     * @param locateLongitude 传来的定位经度
     * @param radius 周围半径
     * @return 停车区列表
     */
    List<Parklot> queryParklotNearby(@Param("map_lat") Double mapLatitude, @Param("map_lng") Double mapLongitude,
                                     @Param("locate_lat") Double locateLatitude, @Param("locate_lng") Double locateLongitude,
                                     @Param("radius") int radius);

    /**
     * 根据停车场id和收费
     *
     * @param parklotId 停车场id
     * @return 应收预约费用
     */
    String queryParklotReserveFeeRule(@Param("parklot_id") Integer parklotId);

    /**
     * 根据停车场id和状态查询预约费用规则
     *
     * @param parklotId 停车区id
     * @return 预约费用规则
     */
    FeeRuleReserve parklotReserveFeeRule(@Param("parklot_id") Integer parklotId);

    /**
     * 根据停车场id和收费
     *
     * @param parklotId 停车场id
     * @return 应收停车费用
     */
    String queryParklotParkingFeeRule(@Param("parklot_id") Integer parklotId);

    /**
     * 根据停车场id和状态查询停车费用规则
     *
     * @param parklotId 停车区id
     * @return 停车费用规则
     */
    FeeRuleParking parklotParkingFeeRule(@Param("parklot_id") Integer parklotId);

    ParkingFeeData queryParkingFeeRuleByOrderId(@Param("orderId") Integer orderId, @Param("state") Integer state);


    /**
     * 查询停车区的最短发布时间间隔
     *
     * @param parklotId          停车去的最短发布时间间隔
     * @param minPublishInterval 最短发布时间key值
     * @return 返回该车场的最短发布时间间隔
     */
    String queryMinPublishTimeInterval(@Param("parklotId") Integer parklotId, @Param("minPublishInterval") String minPublishInterval);

    @Update(value = "UPDATE t_parkloc SET state=#{state}, update_time=UNIX_TIMESTAMP()*1000 WHERE id=(SELECT parkloc_id FROM t_order_parking WHERE id=#{orderId})")
    void updateParklocStateByOrderId(@Param("orderId") int orderId, @Param("state") int state);

    @Select(value = "SELECT parklot_id FROM t_order_parking top WHERE top.id=#{orderId}")
    Integer queryParklotIdByOrderId(@Param("orderId") int orderId);

    @Select(value = "SELECT bpp.qhvalue VALUE FROM b_parklot_params bpp WHERE bpp.parklot_id=#{parklotId} AND bpp.state=#{valid} AND bpp.qhkey=#{key} LIMIT 1")
    Integer queryParklotParamsValueByParklotId(@Param("parklotId") int parklotId, @Param("valid") int valid, @Param("key") String key);

    @Select(value = "SELECT parklot_id FROM t_parkloc WHERE id=#{parklocId}")
    int queryParklotIdByParklocId(@Param("parklocId") int parklocId);

    @Select(value = "SELECT NAME, extra_parklot_id extraParklotId FROM t_parklot WHERE id=#{parklotId}")
    HashMap queryParklotInfo(@Param("parklotId") Integer parklotId);

    /**
     * 查询该订单的车锁最后一条日志
     *
     * @param parklocId
     * @return
     */
    LogLock queryLogLockByParklocId(@Param(value = "parklocId") Integer parklocId);
}
