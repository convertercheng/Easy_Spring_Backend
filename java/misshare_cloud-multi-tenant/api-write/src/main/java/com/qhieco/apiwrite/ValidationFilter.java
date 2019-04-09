package com.qhieco.apiwrite;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/24 上午9:42
 * <p>
 * 类说明：
 *     全局过滤器类，用于验证所有接口的请求参数
 */
@Order(1)
@WebFilter(filterName = "security filter", urlPatterns = "/*")
@Slf4j
public class ValidationFilter implements Filter {

    private static final String LOGIN_API = "/user/login";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        validate(servletRequest);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    /**
     * 处理请求
     * @param servletRequest 客户端发送的请求
     */
    private void validate(ServletRequest servletRequest) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURL = request.getRequestURL().toString();
        // 登录接口不算
        if (!StringUtils.isEmpty(requestURL) && !requestURL.endsWith(LOGIN_API)) {

        }
    }

    @Override
    public void destroy() {

    }
}
