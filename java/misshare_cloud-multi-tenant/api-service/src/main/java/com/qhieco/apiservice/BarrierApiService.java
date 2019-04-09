package com.qhieco.apiservice;

import com.qhieco.barrier.RegisterTempRequest;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import org.springframework.http.ResponseEntity;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 10:59
 * <p>
 * 类说明：
 * 道闸业务层
 */
public interface BarrierApiService {

    /**
     * 临时车登记功能
     * @param jsonContent 临时停车请求json
     * @param url 访问地址
     * @param contentType 内容类型
     * @return 返回请求
     */
    ResponseEntity<String> registeredTemp(String jsonContent, String url, String contentType);

}
