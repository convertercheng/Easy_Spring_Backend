package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.VersionService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Update;
import com.qhieco.commonrepo.UpdateRepository;
import com.qhieco.constant.Status;
import com.qhieco.request.api.UpdateRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 17:25
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
public class VersionServiceImpl implements VersionService {

    @Autowired
    UpdateRepository updateRepository;


    @Override
    public Resp updateInfo(UpdateRequest request) {
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Integer type = request.getType();
        Update update = updateRepository.findLatestInfoByType(type);
        return RespUtil.successResp(update);

    }

}
