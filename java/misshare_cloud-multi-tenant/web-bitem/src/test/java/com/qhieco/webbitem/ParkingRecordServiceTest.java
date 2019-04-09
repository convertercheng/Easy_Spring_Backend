package com.qhieco.webbitem;

import com.qhieco.bitemservice.ParkingRecordService;
import com.qhieco.request.webbitem.ParkingRecordRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/3 19:24
 * <p>
 * 类说明：
 * ${说明}
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ParkingRecordServiceTest {

    @Autowired
    private ParkingRecordService parkingRecordService;

    @Test
    public void queryParkingRecordListTest() {
        ParkingRecordRequest request = new ParkingRecordRequest();
        request.setStart(0);
        request.setLength(10);
        request.setSEcho(0);
        request.setParklotId(11);
        log.info(" ------------- " + parkingRecordService.queryParkingRecordList(request));
    }
}
