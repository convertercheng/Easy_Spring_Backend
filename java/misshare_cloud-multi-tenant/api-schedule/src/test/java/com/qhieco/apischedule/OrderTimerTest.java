package com.qhieco.apischedule;

import com.qhieco.apischedule.timer.OrderTimer;
import com.qhieco.constant.Constants;
import com.qhieco.mapper.OrderMapper;
import com.qhieco.mapper.ParklotAmountMapper;
import com.qhieco.mapper.ParklotMapper;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 20:17
 * <p>
 * 类说明：
 * ${说明}
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
public class OrderTimerTest {
    @Autowired
    private OrderTimer orderTimer;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ParklotAmountMapper parklotAmountMapper;

    @Test
    public void scanOrder(){
        orderTimer.scanOrder();
    }

    @Test
    public void refundTimerTest() {
        orderTimer.refundTimer();
    }

    @Test
    public void queryParklotIdByOrderIdTest() {
//        int id = parklotMapper.queryParklotIdByOrderId(1);
        log.info(" ---------- " + parklotMapper.queryParklotIdByOrderId(452));
    }

    @Test
    public void updateByParklotIdTest(){
        HashMap params = new HashMap();
        params.put("advanceReservationTimeKey", Constants.ADVANCE_RESERVATION_TIME);
        params.put("timeInterval", (TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000));
        params.put("parklotId", 1);
        params.put("now", System.currentTimeMillis());
        parklotAmountMapper.updateByParklotId(params);
    }

    @Test
    public void queryReserveOrderListTest(){
        log.info(" ------------ " + orderMapper.queryReserveOrderList(1301, 1, "max_delay_time"));
    }

    @Test
    public void updateOrderStateAdInvoiceStateByIdTest(){
        orderMapper.updateOrderStateAdInvoiceStateById(1,1,1);
    }
}
