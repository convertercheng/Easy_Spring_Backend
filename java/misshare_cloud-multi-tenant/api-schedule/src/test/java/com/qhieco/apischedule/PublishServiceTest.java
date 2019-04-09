package com.qhieco.apischedule;

import com.qhieco.commonentity.Share;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.ParklocMapper;
import com.qhieco.mapper.PublishMapper;
import com.qhieco.mapper.ShareMapper;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/28 15:31
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PublishServiceTest {
    @Autowired
    private PublishMapper publishMapper;
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private ParklocMapper parklocMapper;

    @Test
    public void updateParklocStateTest() {
        HashMap params = new HashMap();
        params.put("tobealter", Status.Publish.TOBEALTER.getInt());
        params.put("tobecancelled", Status.Publish.TOBECANCELLED.getInt());
        params.put("valid", Status.Common.VALID.getInt());
        params.put("unpublished", Status.Parkloc.UNPUBLISHED.getInt());
        publishMapper.updateParklocState(params);
    }

    @Test
    public void updatePublishOnceTimeoutTest() {
        publishMapper.updatePublishOnceTimeout(1, 2, 3, System.currentTimeMillis());
    }

    @Test
    public void queryValidPublishListTest() {
        log.info(" ------------- " + publishMapper.queryValidPublishList(1, 2, 12));
    }

    @Test
    public void deleteShareByIdTest() {
        shareMapper.deleteShareById(107);
    }

    @Test
    public void insertBatchTest() {
        List<Share> shareList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            shareList.add(new Share(i, i, System.currentTimeMillis(), System.currentTimeMillis(), 1));
        }
        shareMapper.insertBatch(shareList);
    }

    @Test
    public void queryNoPublishedStateParklotsTest(){
        HashMap params = new HashMap();
        params.put("tobealter", Status.Publish.TOBEALTER.getInt());
        params.put("tobecancelled", Status.Publish.TOBECANCELLED.getInt());
        params.put("valid", Status.Common.VALID.getInt());
        params.put("unpublished", Status.Parkloc.UNPUBLISHED.getInt());
        log.info(" ------------- " + publishMapper.queryNoPublishedStateParklots(params));
    }

    @Test
    public void updateParklocStateByParklocIdTest(){
        parklocMapper.updateParklocStateByParklocId(1, 1100);
    }

    @Test
    public void queryReservateTimeListTest() {
        int timeInterval = TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000;
        log.info(" ------------ " + publishMapper.queryReservateTimeList(Constants.ADVANCE_RESERVATION_TIME, timeInterval));
    }
}
