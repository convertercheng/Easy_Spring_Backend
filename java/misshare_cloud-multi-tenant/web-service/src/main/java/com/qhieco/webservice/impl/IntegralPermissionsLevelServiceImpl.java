package com.qhieco.webservice.impl;

import com.qhieco.commonentity.IntegralPermissionsLevel;
import com.qhieco.commonrepo.IntegralPermissionsLevelRepository;
import com.qhieco.constant.Status;
import com.qhieco.webservice.IntegralPermissionsLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 14:24
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
public class IntegralPermissionsLevelServiceImpl implements IntegralPermissionsLevelService {

    @Autowired
    private IntegralPermissionsLevelRepository integralPermissionsLevelRepository;

    @Override
    public List<IntegralPermissionsLevel> findList() {
        return integralPermissionsLevelRepository.findByState(Status.Common.VALID.getInt());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(List<IntegralPermissionsLevel> integralPermissionsLevelList) {
        integralPermissionsLevelList.forEach(integralPermissionsLevel -> {
            integralPermissionsLevelRepository.save(integralPermissionsLevel);
        });
    }
}
