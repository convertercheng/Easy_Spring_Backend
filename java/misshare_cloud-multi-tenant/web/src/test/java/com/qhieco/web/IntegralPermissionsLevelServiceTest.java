package com.qhieco.web;

import com.qhieco.commonentity.IntegralPermissionsLevel;
import com.qhieco.commonrepo.IntegralPermissionsLevelRepository;
import com.qhieco.constant.Status;
import com.qhieco.webservice.IntegralPermissionsLevelService;
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
 * @version 2.0.1 创建时间: 2018/5/24 14:58
 * <p>
 * 类说明：
 * ${说明}
 */
@Slf4j
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class IntegralPermissionsLevelServiceTest {

    @Autowired
    private IntegralPermissionsLevelService integralPermissionsLevelService;

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Test
    public void findListTest() {
        List<IntegralPermissionsLevel> integralPermissionsLevels = integralPermissionsLevelService.findList();
        log.info(" --------- " + integralPermissionsLevels);
    }

    @Test
    public void saveOrUpdate() {
        List<IntegralPermissionsLevel> integralPermissionsLevels = new ArrayList<>();
        for (int i = 0; i < 10; i+=2) {
            IntegralPermissionsLevel integralPermissionsLevel = new IntegralPermissionsLevel();
            integralPermissionsLevel.setId(i+1);
            integralPermissionsLevel.setIntegralPermissionsCoefficient(Double.valueOf(1 + 0.1 * i));
            integralPermissionsLevel.setIntegralPermissionsStart(i * 10 + 1);
            integralPermissionsLevel.setIntegralPermissionsEnd((i + 1) * 10);
            integralPermissionsLevel.setUpdateTime(System.currentTimeMillis());
            integralPermissionsLevel.setState(Status.Common.VALID.getInt());
            integralPermissionsLevel.setIntegralPermissionsRemark("积分等级（" + integralPermissionsLevel.getIntegralPermissionsStart() + "-" + integralPermissionsLevel.getIntegralPermissionsEnd() + ")");
            integralPermissionsLevels.add(integralPermissionsLevel);
        }
        integralPermissionsLevelService.saveOrUpdate(integralPermissionsLevels);
    }

    @Test
    public void queryReserveCoefficientByIntegralTest(){
        Double  reserveCoefficient = integralPermissionsLevelRepository.queryReserveCoefficientByIntegral(10);
        log.info(" ---------- " + reserveCoefficient);
    }
}
