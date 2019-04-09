package com.qhieco.mapper;

import com.qhieco.time.OrderRefundInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 17:43
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface OrderRefundMapper {

    public List<OrderRefundInfo> queryRefundOrderListByCondition(@Param("processing") int processing);

    @Update(value = "update t_order_refund set state=#{state}, update_time=UNIX_TIMESTAMP()*1000 where serial_number=#{serialNumber}")
    public void updateOrderRefundStateBySerialNumber(@Param("serialNumber") String serialNumber, @Param("state") int state);

}
