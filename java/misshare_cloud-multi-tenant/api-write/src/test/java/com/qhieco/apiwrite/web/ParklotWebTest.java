package com.qhieco.apiwrite.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.constant.Constants;
import com.qhieco.mapper.ParklotAmountMapper;
import com.qhieco.request.api.ParklotDetailRequest;
import com.qhieco.request.api.ParklotUsualSetRequest;
import com.qhieco.util.TimeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/27 15:17
 * <p>
 * 类说明：
 * parklot controller的测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ParklotWebTest {

    @Autowired
    private ParklotAmountMapper parklotAmountMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ParklotAmountService parklotAmountService;

    @Test
    public void updateByParklotIdTest() {
        //  更新该车场的parklot_amount表的数据
        HashMap<String, Object> params = new HashMap();
        params.put("advanceReservationTimeKey", Constants.ADVANCE_RESERVATION_TIME);
        params.put("timeInterval", (TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000));
        params.put("parklotId", 1);
        params.put("now", System.currentTimeMillis());
        parklotAmountMapper.updateByParklotId(params);
    }


    @Test
    public void setUsual() throws Exception {
        ParklotUsualSetRequest request = new ParklotUsualSetRequest();
        request.setUser_id(1);
        request.setParklot_id(1);
        request.setTimestamp("1519806690000");
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        java.lang.String requestJson = ow.writeValueAsString(request);
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/parklot/usual/set").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
    }


    @Test
    public void detail() throws Exception {
        ParklotDetailRequest reqest = new ParklotDetailRequest();
        reqest.setParklot_id(1);
        reqest.setTimestamp(String.valueOf(System.currentTimeMillis()));
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
        java.lang.String requestJson = ow.writeValueAsString(reqest);
        String responseString = mockMvc.perform(MockMvcRequestBuilders.post("/parklot/detail").contentType(MediaType.APPLICATION_JSON).content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk()).andDo(print()).andReturn().getResponse().getContentAsString();
        System.out.println("--------返回的json = " + responseString);
    }

    @Test
    public void updateParklotAmountInfoByParklotIdTest(){
        parklotAmountService.updateParklotAmountInfoByParklotId(1, "测试方法");
    }
}