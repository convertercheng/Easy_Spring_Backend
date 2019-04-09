package com.qhieco.web;

import com.qhieco.commonentity.Business;
import com.qhieco.constant.Status;
import com.qhieco.request.web.BusinessRequest;
import com.qhieco.request.web.ParklocRequest;
import com.qhieco.util.FileUploadUtils;
import com.qhieco.webservice.BusinessService;
import com.qhieco.webservice.ParklocService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/3 11:27
 * <p>
 * 类说明：
 * ${description}
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
@Slf4j
public class BusinessTest {

    @Autowired
    private BusinessService businessService;

    @Autowired
    private ParklocService parklocService;

    @Test
    public void testAll(){
        BusinessRequest businessRequest=new BusinessRequest();
        //ParklocRequest parklocRequest=new ParklocRequest();
//        businessRequest.setSEcho(0);
//        businessRequest.setStart(0);
//        businessRequest.setLength(10);
//        System.out.println(businessService.query(businessRequest));
        System.out.println(System.currentTimeMillis());
    }

    @Test
    public void testSave(){

//            Business business=new Business();
//            //business.setId(2);
//            business.setBusinessDescription("测试描述ssssss");
//            business.setBusinessName("测试名称sssssss11111");
//            business.setModifyTime(System.currentTimeMillis());
//            business.setBusinessStatus(Status.Business.BUSINESS_STATUS_END.getInt());
//            System.out.println(businessService.saveUpdate(business));
        String str="D:\\image\\111.jpg";
        String strs=FileUploadUtils.GetImageStrFromPath(str);
       System.out.println( strs);


    }
}
