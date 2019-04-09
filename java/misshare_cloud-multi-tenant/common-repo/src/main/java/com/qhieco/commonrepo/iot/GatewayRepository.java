package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Gateway;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-21 下午4:18
 * <p>
 * 类说明：
 * ${description}
 */
public interface GatewayRepository extends IotRepository<Gateway> {
    @Query("select g.id from Gateway g, Router r where g.routerId = r.id and r.name like :name")
    List<Integer> findIdbyRouterName(@Param("name") String name);

    @Query("select g.id from Gateway g, Router r where g.routerId = r.id and r.number like :number")
    List<Integer> findIdbyRouterNumber(@Param("number") String number);

    @Query("select g.id from Gateway g, Parklot p, Router b where g.routerId = b.id and b.parklotId = p.id and p.name like :name")
    List<Integer> findIdbyParklotName(@Param("name") String name);

    List<Gateway> findByRouterId(Integer id);
}
