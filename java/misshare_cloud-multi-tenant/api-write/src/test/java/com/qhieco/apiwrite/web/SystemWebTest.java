package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.impl.barrier.BoshiBarrierService;
import com.qhieco.apiservice.impl.redis.RedisService;
import com.qhieco.barrier.keytop.response.KeyTopParkingCostRespone;
import com.qhieco.barrier.keytop.response.KeyTopParkingPayCostRespone;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.util.DateUtils;
import com.qhieco.util.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Map;
import java.util.concurrent.DelayQueue;

import static org.junit.Assert.*;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 下午5:45
 * <p>
 * 类说明：
 *     system controller的测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SystemWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BoshiBarrierService boshiBarrierService;

    @Test
    public void getVerificationCode() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/system/verification/code/get"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public  void setValue(){
//      KeyTopParkingPayCostRespone resp=boshiBarrierService.payParkingLotCost("46e8e00a-6446-4a87-bfd1-a82e0136bbd2"
//        ,"18052510455594810023","2","3000.0","1108",20180525111511L);
     //  KeyTopParkingCostRespone resp=boshiBarrierService.getPlateNumberByParkOrderCost("粤B123456",System.currentTimeMillis());
      boolean resp=boshiBarrierService.validateParkingLotNumberInfo("粤B67676","1f9de5bf-2e0f-425f-98a7-a8ef010cb647");
//        String str="<xml><return_code><![CDATA[SUCCESS]]></return_code>\n" +
//                "<return_msg><![CDATA[OK]]></return_msg>\n" +
//                "<appid><![CDATA[wx4cd4cf12579aa5b0]]></appid>\n" +
//                "<mch_id><![CDATA[1503756511]]></mch_id>\n" +
//                "<nonce_str><![CDATA[BzM1bj7dpcPIKJx6]]></nonce_str>\n" +
//                "<sign><![CDATA[185973DF0D103D5B1BCD69B0A0BAE861]]></sign>\n" +
//                "<result_code><![CDATA[FAIL]]></result_code>\n" +
//                "<err_code><![CDATA[NOTENOUGH]]></err_code>\n" +
//                "<err_code_des><![CDATA[基本账户余额不足，请充值后重新发起]]></err_code_des>\n" +
//                "</xml>";
        System.out.println(resp);
    }
}