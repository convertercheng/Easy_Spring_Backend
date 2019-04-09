package com.qhieco.barrier;

import com.qhieco.barrier.service.BarrierService;
import com.qhieco.commonentity.OrderParking;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class BarrierApplicationTests {

    @Autowired
    private BarrierService barrierService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void calculateParkingTotalFeeTest() {
        OrderParking orderParking = new OrderParking();
        orderParking.setParklotId(11);
        orderParking.setReservationId(1441);
        orderParking.setRealStartTime(1525504241000L); // 2018/5/5 15:10:41

        Long realEndTime = System.currentTimeMillis();
        BigDecimal totalFee = barrierService.calculateParkingTotalFee(realEndTime, orderParking);
        log.info("totalFee = " + totalFee);
    }

}
