package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.FeedbackService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.FeedbackRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 下午8:46
 * <p>
 * 类说明：
 *     feedback controller
 */
@RestController
@RequestMapping("feedback")
@Slf4j
public class FeedbackWeb {

    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/add")
    public Resp add(FeedbackRequest feedbackRequest) {
        log.info(" 意见反馈 --- feedbackRequest = " + feedbackRequest);
        Resp resp = ParamCheck.check(feedbackRequest,  "user_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return feedbackService.add(feedbackRequest);
    }
}
