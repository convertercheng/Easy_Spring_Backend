package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.AdvertService;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.mapper.AdvertMapper;
import com.qhieco.response.data.api.AdvertRepData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/24 9:30
 * <p>
 * 类说明：
 * 广告页service
 */
@Service
public class AdvertServiceImpl implements AdvertService {

    @Autowired
    private AdvertMapper advertMapper;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Override
    public AdvertRepData queryAdvert(int phoneType) {
        AdvertRepData advertRepData = advertMapper.queryAdvertInfoByPhoneType(phoneType);
        if (advertRepData != null) {
            advertRepData.setFilePath(configurationFiles.getPicPath() + advertRepData.getFilePath());
        }
        return advertRepData;
    }
}
