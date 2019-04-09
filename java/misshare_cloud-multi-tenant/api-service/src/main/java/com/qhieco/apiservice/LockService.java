package com.qhieco.apiservice;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/7/17 17:30
 * <p>
 * 类说明：
 * ${说明}
 */
public interface LockService {

    public boolean checkLockAvailable(Integer parklocId);
}
