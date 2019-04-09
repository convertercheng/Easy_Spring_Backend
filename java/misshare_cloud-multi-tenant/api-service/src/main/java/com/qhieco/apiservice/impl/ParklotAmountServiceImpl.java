package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.LockService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.constant.Constants;
import com.qhieco.mapper.ParklotAmountMapper;
import com.qhieco.mapper.ParklotParamsMapper;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/28 17:52
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class ParklotAmountServiceImpl implements ParklotAmountService {

    @Autowired
    private ParklotAmountMapper parklotAmountMapper;
    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    private LockService lockService;

    @Override
//    @Async
    public void updateParklotAmountInfoByParklotId(Integer parklotId, String resource) {
        //  更新该车场的parklot_amount表的数据
        String advanceReservationTime = parklotParamsMapper.queryParklotParamsValue(parklotId, Constants.ADVANCE_RESERVATION_TIME);
        Long advanceReservationTimeVALUE = Long.valueOf(StringUtils.isEmpty(advanceReservationTime)
                ? Constants.ADVANCE_RESERVATION_TIME_DEFAULT : advanceReservationTime) * 60 * 1000;
        long now = System.currentTimeMillis();
        long timeInterval = (TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000);
        log.info("查询车场停车位的 请求参数， advanceReservationTimeVALUE = " + advanceReservationTimeVALUE + ", now = " + now
                + ",  parklotId = " + parklotId + "， timeInterval = " + timeInterval);

        int publishedAmount = parklotAmountMapper.queryPublishedAmountByParklotId(parklotId);

        int reserveAmount = parklotAmountMapper.queryUseAmountByParklotId(parklotId);

//        int reservableAmount = parklotAmountMapper.queryReservableAmountByParklotId(parklotId, timeInterval, advanceReservationTimeVALUE, now);
        int reservableAmount = 0;
        List<Integer> parklocIds = parklotAmountMapper.queryReservableParklocIdByParklotId(parklotId, timeInterval, advanceReservationTimeVALUE, now);
        for (Integer parklocId : parklocIds) {
            if (lockService.checkLockAvailable(parklocId)) {
                reservableAmount++;
            }
        }

        log.info("更新车场的parklot_amount数据，parklotId = " + parklotId + ", publishedAmount = " + publishedAmount + " , reserveAmount= "
                + reserveAmount + ", reservableAmount=" + reservableAmount + ", resource = [" + resource + "]");
        parklotAmountMapper.updateParklotAmountByParklotId(parklotId, publishedAmount, reserveAmount, reservableAmount, now);
    }
}
