package com.qhieco.web.tenant;

import com.qhieco.TenantSupport;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.util.TenantContext;
import com.qhieco.util.TenantHelper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class EmployeeServiceAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    // only applicable to employee service
    @Before("@annotation(com.qhieco.anotation.EnableTenantFilter)")
    public void aroundExecution(JoinPoint pjp) throws Throwable {
        Integer tenantId = null;
        if(TenantContext.getCurrentTenant() != null) {
            tenantId = TenantContext.getCurrentTenant();
        }else {
            HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String tenantInfo = req.getParameter("tenantId");
            if(tenantInfo!=null){
                tenantId = Integer.parseInt(tenantInfo);
            }
        }
        if(tenantId !=null) {
            System.out.println("filter enabled! tenant:"+tenantId);
            org.hibernate.Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
            filter.setParameter("tenantId", tenantId);
            filter.validate();
        }
    }

    @AfterReturning(pointcut = "@annotation(com.qhieco.anotation.AddTenantInfo)", returning = "result")
    public void after(Object result){
    //        HttpServletRequest req = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    //        ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
    //        UserWebRespository userWebWebRespository = context.getBean(UserWebRespository.class);
        Object data = ((Resp) result).getData();
        if (data instanceof AbstractPaged){
              ((AbstractPaged)data).getDataList()
                      .forEach((entity)->{
                    if(entity instanceof TenantSupport){
                        TenantSupport tenantSupport = ((TenantSupport)entity);
                        TenantHelper.fillTenantName(tenantSupport);

                    }
              });
        }
        if(data instanceof TenantSupport){
            TenantSupport tenantSupport = ((TenantSupport)data);
            TenantHelper.fillTenantName(tenantSupport);
        }
    }
}
