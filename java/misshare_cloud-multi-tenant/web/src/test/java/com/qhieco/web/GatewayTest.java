package com.qhieco.web;

import com.qhieco.commonentity.iotdevice.Gateway;
import com.qhieco.commonentity.iotdevice.Router;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.request.web.GatewayRequest;
import com.qhieco.request.web.RouterRequest;
import com.qhieco.webservice.RouterService;
import com.qhieco.webservice.impl.GatewayServiceImpl;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-21 下午6:03
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class GatewayTest {

    @Autowired
    GatewayServiceImpl gatewayService;

    @Autowired
    LockRepository lockRepository;

    @Autowired
    RouterService routerService;

    @Test
    public void testQuery(){
//        Gateway gateway = new Gateway();
//        gateway.setIdentifier("123");
////        gateway.setName("wangguan");
//        gatewayService.save(gateway);
        GatewayRequest gatewayRequest = new GatewayRequest();
//        gatewayRequest.setIdentifier("1");
//        gatewayRequest.setParklotName("同方");
        gatewayRequest.setLength(10);
        gatewayRequest.setSEcho(1);
        gatewayRequest.setStart(0);
        System.out.println(gatewayService.query(gatewayRequest));
//        System.out.println(        lockRepository.countByGatwayId(2));
    }

    @Test
    public void testRouter(){
//        Router router = new Router();
//        router.setName("二狗");
//        router.setNumber("0XE1");
//        router.setParklotId(2);
//        routerService.save(router);
        val req = new RouterRequest();
//        req.setParklotName("大厦");
//        req.setModel("1型");
//        req.setManufacturerName("华为");
//        req.setNumber("3");
//        req.setLength(10);
//        req.setSEcho(1);
//        req.setStart(0);
//        System.out.println(routerService.unbindQuery(req));
//        System.out.println(routerService.one(1));
    }
}
