package com.qhieco.web;

import com.qhieco.commonrepo.AdvertRepository;
import com.qhieco.webservice.AdvertService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 13:31
 * <p>
 * 类说明：
 * ${说明}
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class AdvertServiceTest {

    @Autowired
    private AdvertService advertService;

    @Autowired
    private AdvertRepository advertRepository;

    @Test
    public void saveOrUpdateTest() {
        advertService.saveOrUpdate("1", 2, 2, 1, "2", 7, "", "");
    }

    @Test
    public void queryByPhoneTypeTest(){
        log.info(" --------- " + advertService.queryByPhoneType(2));
    }

    @Test
    public void queryPathByIdTest(){
        log.info(" -------- " + advertService.queryPathById(7));
    }

    @Test
    public void deleteByIdTest(){
        advertService.deleteById(1, 7);
    }

    @Test
    public void findPhoneType(){
        log.info(" ---------- " + advertRepository.findByPhoneType(0));
    }
}
