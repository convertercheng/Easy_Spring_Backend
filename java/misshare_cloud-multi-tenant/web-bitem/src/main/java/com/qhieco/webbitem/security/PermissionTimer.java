package com.qhieco.webbitem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-7 下午5:52
 * <p>
 * 类说明：
 * 定时更新权限列表的配置信息
 */
@Component
public class PermissionTimer {
    @Autowired
    MySecurityMetadataService metadataSourceService;

    @Scheduled(fixedRate = 1000*60*10)
    public void updatePermission(){
        metadataSourceService.loadResourceDefine();
    }
}
