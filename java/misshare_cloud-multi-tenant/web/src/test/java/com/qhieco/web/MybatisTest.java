package com.qhieco.web;

import com.qhieco.request.web.ApplyInvoiceRequest;
import com.qhieco.request.web.FeedbackRequest;
import com.qhieco.webmapper.ApplyInvoiceMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-2 下午12:10
 * <p>
 * 类说明：
 * ${description}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class MybatisTest {
    @Autowired
    ApplyInvoiceMapper applyInvoiceMapper;

    @Test
    public void testPage(){
        ApplyInvoiceRequest request = new ApplyInvoiceRequest();
        request.setStart(0);
        request.setLength(10);
        request.setTitle("发票测试");
        System.out.println(applyInvoiceMapper.countBuilder(request));
    }
    @Test
    public void testFeedback(){
        FeedbackRequest request = new FeedbackRequest();
        request.setStart(0);
        request.setLength(1);
//        request.setProblems(Arrays.asList(1,2,3));
    }
}
