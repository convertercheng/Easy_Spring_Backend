package com.qhieco.web.tenant;

import com.qhieco.commonentity.UserWeb;
import com.qhieco.constant.Status;
import com.qhieco.util.TenantContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class TenantFilter implements Filter {
    private static final String TENANT_HEADER = "X-TenantID";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        UserWeb userWeb = (UserWeb)request.getSession().getAttribute("user");
        if(userWeb != null) {
            if (Status.UserWebLevel.COMPANY.getValue().equals(userWeb.getLevel())) {
                System.out.println("tenant father account login!");
                TenantContext.setCurrentTenant(userWeb.getTenantId());
            }
            if (Status.UserWebLevel.SUB_ACCOUNT.getValue().equals(userWeb.getLevel())) {
                System.out.println("tenant son account login!");
                TenantContext.setCurrentTenant(userWeb.getTenantId());
            }
            if (Status.UserWebLevel.PLATFORM.getValue().equals(userWeb.getLevel())) {
                System.out.println("tenant platform account login!");
                TenantContext.clear();
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }
}
