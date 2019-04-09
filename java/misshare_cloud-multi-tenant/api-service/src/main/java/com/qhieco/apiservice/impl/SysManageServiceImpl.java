package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.SysManageService;
import com.qhieco.commonentity.LogOperationMobile;
import com.qhieco.commonrepo.LogOperationMobileRepository;
import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/17 9:24
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class SysManageServiceImpl implements SysManageService {

    @Autowired
    LogOperationMobileRepository logOperationMobileRepository;

    @Override
    public Resp saveUpdateLogApp(LogOperationMobile logOperationMobile) {
        logOperationMobileRepository.save(logOperationMobile);
        return RespUtil.successResp();
    }
}
