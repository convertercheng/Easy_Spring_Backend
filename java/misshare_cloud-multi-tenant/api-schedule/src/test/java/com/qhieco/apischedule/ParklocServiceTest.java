package com.qhieco.apischedule;

import com.qhieco.apiservice.ParklocService;
import com.qhieco.apiservice.ParklotAmountService;
import com.qhieco.constant.Status;
import com.qhieco.mapper.ShareMapper;
import com.qhieco.time.ParklocInfo;
import com.qhieco.time.ParklocShare;
import com.qhieco.time.ParklotIdAdParklocIds;
import com.qhieco.time.ShareTimeOutInfo;
import com.qhieco.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/28 12:46
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class ParklocServiceTest {
    @Autowired
    private ParklocService parklocService;
    @Autowired
    private ShareMapper shareMapper;
    @Autowired
    private ParklotAmountService parklotAmountService;

    @Test
    public void updateParklocAdParklotInfoTest() {
        List<ParklocInfo> parklocInfoList = new ArrayList<>();
        ParklocInfo parklocInfo = new ParklocInfo();
        parklocInfo.setParklocId(1);
        parklocInfoList.add(parklocInfo);
        ParklotIdAdParklocIds parklotIdAdParklocIds = new ParklotIdAdParklocIds();
        parklotIdAdParklocIds.setParklocInfos(parklocInfoList);
        parklotIdAdParklocIds.setParklotId(1);

        List<ParklotIdAdParklocIds> parklotIdAdParklocIdsList = new ArrayList<>();
        for (ParklotIdAdParklocIds parklotIdAdParklocIds1 : parklotIdAdParklocIdsList) {
            parklocService.updateParklocAdParklotInfo(parklotIdAdParklocIds1);
        }
    }

    @Test
    public void shareTimeOutTest(){
//  查询t_share表共享时间过期的数据，更新parklot_amount的数据
        int timeInterval = TimeUtil.getMinChargingPeriod() * 60 * 1000 + TimeUtil.getTimeInterval() * 60 * 1000;
        List<ShareTimeOutInfo> shareTimeOutInfos = shareMapper.queryShareTimeOutList(timeInterval);
        for (ShareTimeOutInfo shareTimeOutInfo : shareTimeOutInfos) {
            log.info("t_share表共享时间过期的数据， shareTimeOutInfo:" + shareTimeOutInfo);
            parklotAmountService.updateParklotAmountInfoByParklotId(shareTimeOutInfo.getParklotId(),"测试方法");

            List<ParklocShare> parklocShareList = shareTimeOutInfo.getParklocShareList();
            if (parklocShareList != null && parklocShareList.size() > 0) {
                shareMapper.updateBatchShareStateByIds(parklocShareList, Status.Common.INVALID.getInt());
            }
        }
    }

}
