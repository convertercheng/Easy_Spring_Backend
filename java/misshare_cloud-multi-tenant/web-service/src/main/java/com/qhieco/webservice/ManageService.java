package com.qhieco.webservice;

import com.qhieco.request.web.FeedbackRequest;
import com.qhieco.response.Resp;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-3 上午11:27
 * <p>
 * 类说明：
 * ${description}
 */
public interface ManageService {
    Resp feedback(FeedbackRequest request);

    Resp feedbackExcel(FeedbackRequest feedbackRequest, OutputStream outputStream) throws IOException;
}
