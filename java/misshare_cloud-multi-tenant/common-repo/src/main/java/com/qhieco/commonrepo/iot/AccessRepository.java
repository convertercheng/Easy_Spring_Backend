package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Access;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 下午4:48
 * <p>
 * 类说明：
 * ${description}
 */
public interface AccessRepository extends IotRepository<Access>{

    @Query("select a from Access a where a.parklotId=:parklotId and a.state=:state")
    List<Access> findAccessByparklotId(@Param("parklotId") Integer parklotId, @Param("state") Integer state);
}
