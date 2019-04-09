package com.qhieco.web;

import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.commonrepo.ParklotRepository;
import com.qhieco.request.web.BarrierRequest;
import com.qhieco.response.Resp;
import com.qhieco.webservice.impl.BarrierServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 下午5:17
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class BarrierServiceTest {

    @Autowired
    BarrierServiceImpl barrierService;

    @Autowired
    ParklotRepository parklotRepository;

    @Test
    public void testSave(){
        Barrier barrier = new Barrier();
//        query.setId(1);
        barrier.setName("大时代北门");
        barrier.setServerIp("10.1.1.1");
        barrier.setManufacturerName("海康");
        barrier.setPassword("dafdsf");
        barrierService.save(barrier);
    }

    @Test
    public void testPage(){
        BarrierRequest barrier = new BarrierRequest();
        barrier.setSEcho(1);
        barrier.setLength(10);
        barrier.setStart(0);
       barrier.setServerIp("1");
       // barrier.setParklotName("sdafs");
        System.out.println(barrierService.query(barrier));
    }

//    @Test
    public void testBind(){
        Integer[] barrierIds = {1,2};
        barrierService.bind(barrierIds, 1 );
    }

    @Test
    public void testFind(){
        BarrierRequest barrierRequest = new BarrierRequest();
        barrierRequest.setName("dfsaf");
        Resp<List<Barrier>> barrierResp = barrierService.unbindQuery(barrierRequest);
        System.out.println(barrierResp);
    }
}
