package com.qhieco.commonrepo.iot;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-14 上午11:59
 * <p>
 * 类说明：
 * ${description}
 */
public interface IotBaseRepository<T> {

    List<T> searchByProperty(String field, Object value, Class<T> cls);

    List<T> hqlExcecute(String hqlStr);
}
