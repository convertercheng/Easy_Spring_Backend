package com.qhieco.commonrepo.iot;

import com.qhieco.commonentity.iotdevice.AbstractIotDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-13 下午4:28
 * <p>
 * 类说明：
 * ${description}
 */

public interface IotRepository<T extends AbstractIotDevice> extends JpaRepository<T, Integer>,JpaSpecificationExecutor<T> {
}
