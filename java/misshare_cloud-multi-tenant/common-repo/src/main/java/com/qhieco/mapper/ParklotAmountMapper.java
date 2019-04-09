package com.qhieco.mapper;

import com.qhieco.response.data.api.OrderParkingData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.HashMap;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 13:49
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface ParklotAmountMapper {

    /**
     * 更新停车区统计表
     */
    void updateByParklotId(HashMap params);

    /**
     * 更新所有车场的
     * publish_amount   停车位发布数量
     * idle_amount    停车位闲置数量
     * reserved_amount    停车位已预约数量
     */
//    public void updateAllParklotAmount(HashMap params);

    /**
     * 查询车场已发布车位数
     *
     * @param parklotId
     * @return
     */
    public int queryPublishedAmountByParklotId(@Param("parklotId") Integer parklotId);


    public List<OrderParkingData> query1();
    public List<OrderParkingData> query2();
    public int query3(@Param("parklotId") Integer parklotId);

    /**
     * 查询车场已预约车位数 = 预约阶段的车位数 + 正在停车阶段的车位数
     *
     * @param parklotId
     * @return
     */
    public int queryUseAmountByParklotId(@Param("parklotId") Integer parklotId);

    /**
     * 查询车场可预约车位数
     *
     * @param parklotId
     * @return
     */
    public int queryReservableAmountByParklotId(@Param("parklotId") Integer parklotId, @Param("timeInterval") long timeInterval,
                                                @Param("advanceReservationTimeVALUE") long advanceReservationTimeVALUE, @Param("now") long now);
    /**
     * 查询车场可预约车位的车位id
     *
     * @param parklotId
     * @return
     */
    public List<Integer> queryReservableParklocIdByParklotId(@Param("parklotId") Integer parklotId, @Param("timeInterval") long timeInterval,
                                                             @Param("advanceReservationTimeVALUE") long advanceReservationTimeVALUE, @Param("now") long now);

    /**
     * 更新车场车位数据
     *
     * @param parklotId
     * @param publishAmount
     * @param reservedAmount
     * @param reservableAmount
     * @param now
     */
    public void updateParklotAmountByParklotId(@Param("parklotId") Integer parklotId, @Param("publishAmount") int publishAmount,
                                               @Param("reservedAmount") int reservedAmount, @Param("reservableAmount") int reservableAmount,
                                               @Param("now") long now);
}
