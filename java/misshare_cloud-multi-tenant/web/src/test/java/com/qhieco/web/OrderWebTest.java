package com.qhieco.web;

import com.qhieco.commonentity.OrderWithdraw;
import com.qhieco.request.web.OrderRefundRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.request.web.OrderWithdrawRequest;
import com.qhieco.webmapper.OrderRefundMapper;
import com.qhieco.webmapper.OrderWithdrawMapper;
import com.qhieco.webservice.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午9:23
 *          <p>
 *          类说明：
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class OrderWebTest {
    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    OrderRefundMapper orderRefundMapper;
    @Autowired
    OrderWithdrawMapper orderWithdrawMapper;

    @Test
    public void parkingOrder() throws Exception {
        OrderRequest orderRequest = new OrderRequest();
//        List<Integer> stateList = new ArrayList<>();
//        stateList.add(1302);
//        stateList.add(1300);
//        orderRequest.setSEcho(1);
//        orderRequest.setLength(10);
//        orderRequest.setStart(0);
//        orderRequest.setStateList(stateList);
        List<Integer> list=new ArrayList<Integer>();
        list.add(1309);
        orderRequest.setId(5);
        orderRequest.setLength(5);
        orderRequest.setSEcho(1);
        orderRequest.setStart(0);
        orderRequest.setStateList(list);
        log.info("pageable: {}", orderService.orderPage(orderRequest));
    }

    @Test
    public void one() {
        int id = 4135;
        log.info("details: {}", orderService.one(id));
    }

    @Test
    public void refundOrder(){
        OrderRefundRequest request  = new OrderRefundRequest();
        request.setPhone("17328766153");
        request.setStart(0);
//        request.setStartCreateTime(new Long(1));
//        request.setEndCreateTime(new Long(2));
        request.setLength(10);
        request.setSEcho(1);
        System.out.println(orderRefundMapper.orderPage(request));
    }

    @Test
    public void withdrawOrder(){
        OrderWithdrawRequest request = new OrderWithdrawRequest();
        request.setPhone("17328766153");
        request.setStart(0);
//        request.setStartCreateTime(new Long(1));
//        request.setEndCreateTime(new Long(2));
        request.setLength(10);
        request.setSEcho(1);
        System.out.println(orderWithdrawMapper.orderPage(request));
    }
}
