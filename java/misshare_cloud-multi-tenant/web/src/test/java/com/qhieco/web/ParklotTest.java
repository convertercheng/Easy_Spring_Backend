package com.qhieco.web;

import com.qhieco.commonentity.Area;
import com.qhieco.commonentity.Parkloc;
import com.qhieco.commonentity.ParklotAmount;
import com.qhieco.request.web.FeeRequest;
import com.qhieco.request.web.ParklocRequest;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.webservice.AreaService;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.ParklocService;
import com.qhieco.webservice.ParklotService;
import com.qhieco.webservice.impl.ParklotServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-26 下午9:13
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ParklotTest {
    @Autowired
    ParklotService parklotService;

    @Autowired
    ParklocService parklocService;
    @Autowired
    FeeRuleService feeRuleService;

    @Autowired
    AreaService areaService;

    @Test
    public void testQuery(){
        ParklotRequest parklotRequest = new ParklotRequest();
        parklotRequest.setLength(10);
        parklotRequest.setSEcho(1);
        parklotRequest.setStart(0);
        parklotRequest.setName("同");
       System.out.println(parklotService.query(parklotRequest));

    }

    @Test
    public void testParkloc(){
        ParklocRequest parklocRequest = new ParklocRequest();

        parklocRequest.setLength(10);
        parklocRequest.setSEcho(1);
        parklocRequest.setStart(0);
//        parklocRequest.setParklotName("同");
        System.out.println(parklocService.query(parklocRequest));
    }

    @Test
    public void testFee(){
        FeeRequest request = new FeeRequest();

        request.setLength(10);
        request.setSEcho(1);
        request.setStart(0);
        request.setName("商场");
//        System.out.println(feeRuleService.queryReserveFee(request));
        System.out.println(feeRuleService.queryParkingFee(request));
    }

    @Test
    public void Strings(){
        System.out.print(parklotService.findOne(1));
    }
}
