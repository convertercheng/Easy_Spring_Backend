package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.response.data.api.LockInfoRepData;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-16 下午4:04
 * <p>
 * 类说明：
 * ${description}
 */
public interface LockRepository extends IotRepository<Lock> {
    /**
     * 统计网关下辖车位锁数量
     * @param gatewayId
     * @return
     */
    @Query("select count(l) from Lock l where l.gatewayId = :gatewayId")
    Integer countByGatwayId(@Param("gatewayId") Integer gatewayId);

    /**
     * 通过编号寻找车位
     * @param number
     * @return
     */
    @Query("select parklocId from Lock where serialNumber like ?1")
    List<Integer> findParklocByNumber(String number);

    /**
     * 通过车位id找车锁
     * @param parklocId
     * @return
     */
    List<Lock> findByParklocId(Integer parklocId);

    /**
     * 根据Mac更新电量
     * @param mac
     * @param battery
     * @return
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Lock set battery = :battery where mac = :mac")
    void updateBatteryByMac(@Param("mac") String mac, @Param("battery") Double battery);

    /**
     * 根据parkloc_id，置空
     * @param parklocId
     * @return
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    @Query("update Lock set parklocId = null where parklocId = :parklocId")
    void updateParklocIdByParklocId(@Param("parklocId") Integer parklocId);

    /**
     * 跟据mac查询LOCK
     * @param mac
     * @return
     */
    Lock findByMac(String mac);

    /**
     * 跟据mac查询LOCK
     * @param mac
     * @return
     */
    @Query("select k from Lock k where k.btName =?1")
    Lock findByBtName(String mac);

    /**
     * 查询所有绑定过车锁的网关ID
     * @return
     */
    @Query("select k.gatewayId from Lock k where k.gatewayId is not null ")
    List<Integer> findAllByGatewayId();

    /**
     * 统计电量过低的车位锁数量
     * @param battery　电量
     * @return
     */
    @Query("select count(l.id) from Lock l where l.battery < :battery")
    Integer countNoPower(@Param("battery") Double battery);

    @Query("select count(l.id) from Lock l where l.state = :state")
    Integer countInvalid(@Param("state") Integer state);

    @Query(value = "select l.bt_mac as lockMac,g.identifier as identifier from t_lock as l inner join t_gateway as g on l.gateway_id = g.id where l.id in(:ids)",nativeQuery = true)
    List<Object[]> findLockInfoList(@Param("ids") List<Integer> ids);
}
