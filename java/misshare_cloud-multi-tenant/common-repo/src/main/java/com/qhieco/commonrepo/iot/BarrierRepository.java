package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Barrier;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 上午11:09
 * <p>
 * 类说明：
 * 道闸数据库交互层
 */
public interface BarrierRepository extends IotRepository<Barrier> {

    @Query("select b from Barrier b where b.parklotId=:parklotId and b.state=:state")
    List<Barrier> findByparklotId(@Param("parklotId") Integer parklotId, @Param("state") Integer state);
    
}
