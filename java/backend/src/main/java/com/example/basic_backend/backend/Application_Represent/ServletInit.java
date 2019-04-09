package com.example.basic_backend.backend.Application_Represent;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @程志豪 时间：15:55
 * 描述：{Servlet的定义和初始化}
 **/
@WebServlet(urlPatterns = "/user")
public class ServletInit extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
        System.out.println("doget");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().print("<h1>Hello MyServlet Response return you this</h1>");
        super.doPost(req, resp);
        System.out.println("dopost");
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }
}
