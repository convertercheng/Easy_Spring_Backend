package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Router;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-21 下午6:45
 * <p>
 * 类说明：
 * ${description}
 */
public interface RouterRepository extends IotRepository<Router>{

    @Query("select g.id from Router g, Parklot p where g.parklotId = p.id and p.name like :name")
    List<Integer> findIdbyParklotName(@Param("name") String name);
}
