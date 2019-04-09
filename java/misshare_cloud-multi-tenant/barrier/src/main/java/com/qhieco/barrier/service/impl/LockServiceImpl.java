package com.qhieco.barrier.service.impl;

import com.qhieco.barrier.service.LockService;
import com.qhieco.constant.Status;
import com.qhieco.mapper.LockMapper;
import com.qhieco.mapper.LogLockMapper;
import com.qhieco.response.data.api.LockTypeData;
import com.qhieco.response.data.api.LogLockInfoData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/17 17:30
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class LockServiceImpl implements LockService {

    private BigDecimal batteryStandard = new BigDecimal(0.10);

    @Autowired
    private LockMapper lockMapper;

    @Autowired
    private LogLockMapper logLockMapper;

    /**
     * 判断车位对应的车位锁是否正常， 正常的则可以预约
     *
     * @param parklocId
     * @return true:可预约
     * false:不可预约
     */
    @Override
    public boolean checkLockAvailable(Integer parklocId) {
        LockTypeData lockTypeData = lockMapper.queryIdbyParklocId(parklocId);
        if (lockTypeData == null) {
            return true;
        }
        Integer lockId = lockTypeData.getLockId();
        Integer lockType = lockTypeData.getLockType();

        if (Status.Lock.LOCK_BT.getInt().equals(lockType) || Status.Lock.LOCK_LORA.getInt().equals(lockType)) {
            return true;
        }
        // 查询该锁有没有日志
        int count = logLockMapper.queryCountLogLockByLockId(lockId);
        if (count > 0) {
            // 60 分钟
            Long time = 3600000L;
            // 24小时
            if (Status.Lock.LOCKNB.getInt().equals(lockType)) {
                time = 86400000L;
            }
            LogLockInfoData logLockInfoData = logLockMapper.queryLockLogInfo(lockId, time);
            if (logLockInfoData == null) {
                log.error("该车位 " + time + " 内车锁日志为空，不能预约该车位，parklocId = " + parklocId);
                return false;
            }
//            if (3 == logLockInfoData.getRockerState() || logLockInfoData.getBattery().compareTo(batteryStandard) < 0) {
//                log.error("车位锁摇臂状态异常  或者  车位锁电量不足10% ：" + logLockInfoData);
//                return false;
//            }
            BigDecimal avgBatteryLimit5 = logLockMapper.queryAvgBatteryLimit5(lockId);
            if (avgBatteryLimit5.compareTo(batteryStandard) < 0) {
                log.error("该车位最近五次日志记录平均电量不足10%， " + logLockInfoData);
                return false;
            }
        }
        return true;
    }
}
