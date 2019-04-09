package com.qhieco.web;


import com.qhieco.constant.Status;
import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.webservice.ApplyService;
import com.qhieco.webservice.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 10:20
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class ApplyTest {

    @Autowired
    private ApplyService applyService;


    @Test
    public  void testAll(){
//        ApplyParklocRequest couponRequest=new ApplyParklocRequest();
//        couponRequest.setSEcho(0);
//        couponRequest.setStart(0);
//        couponRequest.setLength(2);
//        //couponRequest.setCouponCode("H");
//        //couponRequest.setMinCouponLimit(new BigDecimal(12));
//       // couponRequest.setMaxCouponLimit(new BigDecimal(20));
//       // couponRequest.setMinUsedMoney(new BigDecimal(0));
//       // couponRequest.setMaxUsedMoney(new BigDecimal(20));
//        couponRequest.setState(1);

        System.out.println();
    }

}
