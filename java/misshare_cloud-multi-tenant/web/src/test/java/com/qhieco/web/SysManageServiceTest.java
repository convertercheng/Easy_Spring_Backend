package com.qhieco.web;

import com.qhieco.request.web.FeekbackRequest;
import com.qhieco.request.web.LogOperationWebRequest;
import com.qhieco.webservice.SysManageService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 14:29
 * <p>
 * 类说明：
 * ${说明}
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class SysManageServiceTest {

    @Autowired
    private SysManageService sysManageService;

    @Test
    public void queryFeekbackListTest() {
        List<Integer> proIdList = new ArrayList<>();
//        proIdList.add(1);
//        proIdList.add(2);
        log.info("  ------------ " + sysManageService.queryFeekbackList("", null, null, proIdList, 0, 10, 0));
    }

    @Test
    public void queryLogOperateListTest() {
        // log.info(" ------ " + sysManageService.queryLogOperateList(0,10));
    }

    @Test
    public void queryLogLoginListTest() {
        log.info(" --------- " + sysManageService.queryLogLoginList("", "", null, null, 0, 10, 0));
    }

    @Test
    public void operationWebListExcelTest() throws Exception {
        LogOperationWebRequest logOperationWebRequest = new LogOperationWebRequest();
        log.info(" ----- " + sysManageService.operationWebListExcel(logOperationWebRequest, new FileOutputStream("D:\\desktop\\test.txt")));
    }

    @Test
    public void queryFeedbackListByConditionExcelTest() throws Exception {
        FeekbackRequest feekbackRequest = new FeekbackRequest();
        log.info(" --------- " + sysManageService.queryFeedbackListByConditionExcel(feekbackRequest, new FileOutputStream("D:\\desktop\\test.txt")));
    }
}
