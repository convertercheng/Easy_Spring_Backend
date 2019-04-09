package com.qhieco.commonrepo;

import com.qhieco.commonentity.LogRequestApp;
import com.qhieco.commonentity.LogRequestWeb;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/23 上午1:31
 * <p>
 * 类说明：
 *   LogRequestApp
 */

public interface LogRequestAppRepository extends JpaRepository<LogRequestApp, Integer>{

    List<LogRequestApp> findByMethodAndUri(String method, String uri);
}
