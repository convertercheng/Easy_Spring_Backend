package com.qhieco.webservice;

import com.qhieco.commonentity.IntegralPermissionsLevel;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/5/24 14:12
 * <p>
 * 类说明：
 * ${说明}
 */
public interface IntegralPermissionsLevelService {

    public List<IntegralPermissionsLevel> findList();

    public void saveOrUpdate(List<IntegralPermissionsLevel> integralPermissionsLevelList);
}
