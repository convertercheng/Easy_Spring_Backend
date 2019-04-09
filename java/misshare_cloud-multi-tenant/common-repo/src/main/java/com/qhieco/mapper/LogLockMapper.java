package com.qhieco.mapper;

import com.qhieco.response.data.api.LogLockInfoData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/17 11:33
 * <p>
 * 类说明：
 * ${说明}
 */
@Mapper
public interface LogLockMapper {

    @Select(value = "SELECT AVG(temp.`battery`) FROM (SELECT tll.`battery` FROM  t_log_lock tll WHERE tll.`lock_id`=#{lockId} ORDER BY id DESC LIMIT 5) temp")
    public BigDecimal queryAvgBatteryLimit5(@Param("lockId") Integer lockId);

    public LogLockInfoData queryLockLogInfo(@Param("lockId") Integer lockId, @Param("time") Long time);

    @Select(value = "SELECT COUNT(1) FROM t_log_lock tll WHERE tll.`lock_id`=#{lockId}")
    public Integer queryCountLogLockByLockId(@Param("lockId") Integer lockId);
}
