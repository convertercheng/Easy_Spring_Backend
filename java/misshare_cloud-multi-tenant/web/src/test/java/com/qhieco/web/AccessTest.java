package com.qhieco.web;

import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.commonrepo.iot.IotRepository;
import com.qhieco.request.web.AccessRequest;
import com.qhieco.webservice.impl.AccessServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-13 下午4:29
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class AccessTest {
    @Autowired
    IotRepository<Access> accessRepository;

    @Autowired
    AccessServiceImpl accessService;

//    @Test
    public void testSave(){
        Access access = new Access();
        access.setName("aaaa");
        access.setBtName("123");
        access.setBtPwd("123");
        access.setState(1);
        access.setIntro("fasdf");
        access.setUpdateTime(System.currentTimeMillis());
//        IotLock lock = new IotLock();
//        lock.setBattery(1.);
//        lock.setBtPassword("asf");
//        lock.setGatewayId(1);
//        lock.setMac("fasd");
//        lock.setState(1);
//        lock.setUpdateTime(System.currentTimeMillis());
//        lock.setSerialNumber("fdsa");
        System.out.println(accessService.save(access));

    }

//    @Test
//    public void testQuery(){
//        List<IotAccess> accessList = accessRepository.searchByProperty("name","bb",IotAccess.class);
//        System.out.println(accessList);
//    }

//    @Test
//    public void testHql(){
//        String hql = "SELECT a FROM IotAccess a";
//        List<IotAccess> accessList = accessRepository.hqlExcecute(hql);
//        System.out.println(accessList);
//    }

//    @Test
    public void testService(){
        Access access = new Access();
        access.setName("112232");
        access.setBtName("123");
        access.setBtPwd("123");
        access.setState(1);
        access.setIntro("fasdf");
        access.setUpdateTime(System.currentTimeMillis());
        accessService.save(access);
    }



    @Test
    public void testAll(){
        AccessRequest accessRequest = new AccessRequest();
        //accessRequest.setName("hhhhhhhhhhhh");
        accessRequest.setLength(1);
        accessRequest.setSEcho(1);
        accessRequest.setStart(0);
        System.out.println(accessService.query(accessRequest));

    }
}
