package com.qhieco.util;

import com.qhieco.TenantSupport;
import com.qhieco.commonentity.Tenant;
import com.qhieco.commonrepo.TenantRepository;
import com.qhieco.commonrepo.UserWebRespository;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 19-1-8 下午5:06
 * <p>
 * 类说明：
 * ${description}
 */
public class TenantHelper {

    public static <T extends TenantSupport> void fillTenantName(T tenantSupport){
        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
//        UserWebRespository userWebWebRespository = context.getBean(UserWebRespository.class);
        TenantRepository tenantRepository = context.getBean(TenantRepository.class);
        if(tenantSupport.getTenantId()!=null){
            Tenant tenant = tenantRepository.findOne(tenantSupport.getTenantId());
            if(tenant!=null) {
                tenantSupport.setTenantName(tenant.getName());
            }
        }
    }

    public static <T extends TenantSupport> void fillTenantName(List<T> tenantSupportList){
        tenantSupportList.forEach(TenantHelper::fillTenantName);
    }
}
