package com.qhieco.apiread.web;

import com.qhieco.apiservice.AdvertService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.AdvertRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 16:00
 * <p>
 * 类说明：
 * 广告页controller
 */
@Controller
@RequestMapping(value = "advert")
@Slf4j
public class AdvertWeb {

    @Autowired
    private AdvertService advertService;

    /**
     * 获取广告页信息
     */
    @PostMapping(value = "get")
    @ResponseBody
    public Resp advert(@RequestBody AdvertRequest advertRequest) {
        if (advertRequest.getPhone_type() == null) {
            log.error("获取广告页接口，手机类型参数为空， 调用失败。");
            throw new QhieException(Status.ApiErr.PHONE_TYPE_UNKNOWN);
        }
        return RespUtil.successResp(advertService.queryAdvert(advertRequest.getPhone_type()));
    }

}
