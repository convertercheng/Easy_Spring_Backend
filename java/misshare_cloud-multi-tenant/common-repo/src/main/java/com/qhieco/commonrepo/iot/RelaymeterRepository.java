package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.commonentity.iotdevice.Relaymeter;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-20 上午11:05
 * <p>
 * 类说明：
 * ${description}
 */
public interface RelaymeterRepository extends IotRepository<Relaymeter> {

    @Query("select r from Relaymeter r where r.parklotId=:parklotId and r.state=:state")
    List<Relaymeter> findRelaymeterByparklotId(@Param("parklotId") Integer parklotId, @Param("state") Integer state);

    @Query("select r from Relaymeter r where r.name=?1 and r.state=?2")
    Relaymeter findRelaymeterByName(String name, Integer state);

    @Query("select r from Relaymeter r where r.name=?1 and r.state=?2 and r.id <>?3")
    Relaymeter findRelaymeterByName(String name, Integer id, Integer state);
}
