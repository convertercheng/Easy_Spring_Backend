package com.qhieco.apiread.web;

import com.qhieco.apiservice.FeedbackService;
import com.qhieco.request.api.FeedbackRequest;
import com.qhieco.response.Resp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 下午8:46
 * <p>
 * 类说明：
 * problem controller
 */
@RestController
@RequestMapping("problem")
@Slf4j
public class ProblemWeb {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/all")
    public Resp all(@RequestBody FeedbackRequest feedbackRequest) {
        return feedbackService.problemAll(feedbackRequest);
    }
}
