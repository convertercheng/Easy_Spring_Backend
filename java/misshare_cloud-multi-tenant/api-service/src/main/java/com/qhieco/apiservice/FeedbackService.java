package com.qhieco.apiservice;

import com.qhieco.request.api.FeedbackRequest;
import com.qhieco.response.Resp;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 10:59
 * <p>
 * 类说明：
 * 反馈业务层
 */
public interface FeedbackService {

    /**
     * 查询问题列表
     *
     * @param feedbackRequest
     * @return
     */
    Resp problemAll(FeedbackRequest feedbackRequest);

    /**
     * 添加意见反馈
     *
     * @param feedbackRequest
     * @return
     */
    Resp add(FeedbackRequest feedbackRequest);


}
