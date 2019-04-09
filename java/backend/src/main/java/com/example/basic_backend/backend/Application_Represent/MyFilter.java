package com.example.basic_backend.backend.Application_Represent;

import jdk.nashorn.internal.ir.RuntimeNode;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @程志豪 时间：16:29
 * 描述：{责任链和过滤器}
 **/
public class MyFilter {
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)resp;

        HttpSession session = request.getSession();

        String url = "/user/login";
        response.sendRedirect(url);
        System.out.println("重定向");
        return;

    }

    public void init(FilterConfig filterConfig){}


    public void destory(){}
}
