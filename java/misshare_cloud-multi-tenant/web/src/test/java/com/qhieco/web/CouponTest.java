package com.qhieco.web;

import com.qhieco.commonrepo.UserExtraInfoRepository;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.util.DateUtils;
import com.qhieco.util.FileUploadUtils;
import com.qhieco.webmapper.CouponMapper;
import com.qhieco.webservice.CouponService;
import freemarker.template.utility.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Decoder;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.*;

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
public class CouponTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Test
    public  void testAll()throws IOException{
        BASE64Decoder decoder = new BASE64Decoder();

        StringBuffer stringBuffer=new StringBuffer("MzIxODcwODk4NQ");
        System.out.println(decoder.decodeBuffer(stringBuffer.toString()));
      //  System.out.println(userExtraInfoRepository.findByUserExtraInfo("ss"));
//        CouponRequest couponRequest=new CouponRequest();
//        couponRequest.setSEcho(0);
//        couponRequest.setStart(0);
//        couponRequest.setLength(10);
//        couponRequest.setUserPhone("7832");
        //couponRequest.setCouponCode("H");
        //couponRequest.setMinCouponLimit(new BigDecimal(12));
       // couponRequest.setMaxCouponLimit(new BigDecimal(20));
       // couponRequest.setMinUsedMoney(new BigDecimal(0));
       // couponRequest.setMaxUsedMoney(new BigDecimal(20));
       // couponRequest.setState(1);
       // couponRequest.setMinUsedMoney(new BigDecimal(10));
//        System.out.println(couponService.userCouponQuery(couponRequest));
    }

    @Test
    public  void faquan(){
        CouponRequest couponRequest=new CouponRequest();
        couponRequest.setCouponNum(10);
        List<Integer> list=new ArrayList<Integer>();
        list.add(14);
        list.add(13);
        couponRequest.setBeginTime(System.currentTimeMillis());
        couponRequest.setEndTime(System.currentTimeMillis());
        couponRequest.setCouponLimit(new BigDecimal(10));
        couponRequest.setOperationType(1);
       couponRequest.setTagList(list);
//        list.add(3);
//        list.add(4);
//        list.add(5);
  //      couponRequest.setUserIdList(list);
        System.out.print(couponService.save(couponRequest));
    }

    @Test
    public void test()throws DateUtil.DateParseException {
//        String id="";
//        Integer ids=StringUtils.isEmpty(id) ? null : Integer.valueOf(id);
//        System.out.println(ids);
        String str="D://image/111.jpg";
        String ss=FileUploadUtils.GetImageStrFromPath(str);
        System.out.println(FileUploadUtils.uploadImage("D://image1/",ss,System.currentTimeMillis()+""));
    }
}
