package com.qhieco.web;

import com.qhieco.commonentity.Activity;
import com.qhieco.request.web.ActivityQuery;
import com.qhieco.request.web.PrizeReceiveListRequest;
import com.qhieco.webmapper.FileMapper;
import com.qhieco.webservice.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 10:51
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ActivityServiceTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private FileMapper fileMapper;
    @Autowired
    MockHttpServletResponse response;

    @Test
    public void deleteActivityTest() {
        activityService.deleteActivity(2);
    }

    @Test
    public void queryActivityListTest() {
        ActivityQuery query = new ActivityQuery();
        query.setStart(0);
        query.setSEcho(0);
        query.setLength(20);
        query.setName("nam");
        query.setState(1);
        query.setType(1);
        log.info(" ----- " + activityService.queryActivityList(query));
    }

    @Test
    public void queryActivityDetailInfoTest() {
        log.info(" ------ " + activityService.queryActivityDetailInfo(1));
    }

    @Test
    public void saveOrUpdateTest() {
        try {
            Activity activity = new Activity();
            /*log.info(" ----- " + activityService.saveOrUpdate(activity, null, null, null, null));*/
        } catch (Exception e) {
            log.error(" --- " + e);
        }
    }

    @Test
    public void updateActivityFileBTest() {
        fileMapper.updateActivityFileB(1, 2);
    }

    @Test
    public void insertActivityFileBTest() {
        fileMapper.insertActivityFileB(1, 2, 2);
    }

    @Test
    public void queryPrizeReceiveListTest() {
        PrizeReceiveListRequest receiveListRequest = new PrizeReceiveListRequest();
        receiveListRequest.setStart(0);
        receiveListRequest.setSEcho(0);
        receiveListRequest.setLength(20);
//        receiveListRequest.setPhone("150182202523");
        receiveListRequest.setPrizeName("活动12");
        receiveListRequest.setTime(20180611L);
        receiveListRequest.setTriggerType(1);
        log.info(" ----------------- " + activityService.queryPrizeReceiveList(receiveListRequest));
    }

    @Test
    public void queryValidPrizeAllTest() {
        log.info(" ------------------ " + activityService.queryValidPrizeAll(1L, 1L));
    }

    @Test
    public void deletePrizeTest() {
        log.info(" ------" + activityService.deletePrize(1));
    }
    @Test
    public void unfreezePrizeTest() {
        log.info(" ------" + activityService.unfreezePrize(14));
    }

    @Test
    public void queryPrizeReceiveListExcelTest() throws Exception{
        PrizeReceiveListRequest prizeReceiveListRequest = new PrizeReceiveListRequest();
        prizeReceiveListRequest.setPrizeName("0");
        activityService.queryPrizeReceiveListExcel(prizeReceiveListRequest, response.getOutputStream());
    }

    @Test
    public void queryPrizeDetailByIdTest(){
        log.info(" ------------- " + activityService.queryPrizeDetailById(1));
    }

}
