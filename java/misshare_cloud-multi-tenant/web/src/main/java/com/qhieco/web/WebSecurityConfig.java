package com.qhieco.web;

import com.google.gson.Gson;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import com.qhieco.web.security.CustomUserService;
import com.qhieco.web.security.MyFilterSecurityInterceptor;
import com.qhieco.webservice.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import java.io.PrintWriter;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-4-4 下午2:42
 * <p>
 * 类说明：
 * ${description}
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Autowired
    private AdminService adminService;

    @Bean
    UserDetailsService customUserService(){ //注册UserDetailsService 的bean
        return new CustomUserService();
    }

    /**
     * user Details Service验证
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService());

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin()
                .loginPage("/auth/unLogin")
                .loginProcessingUrl("/user/login")
                .usernameParameter("username").passwordParameter("password").permitAll()
                .failureHandler(
                        (httpServletRequest, httpServletResponse, e) -> {
                            Resp resp;
                            if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
                                 resp=  RespUtil.errorResp(Status.WebErr.WRONG_NAME_PASSWORD.getCode(),Status.WebErr.WRONG_NAME_PASSWORD.getMsg());
                            } else {
                                resp =  RespUtil.errorResp(Status.WebErr.LOGIN_FAILD.getCode(),Status.WebErr.LOGIN_FAILD.getMsg());
                            }
                            httpServletResponse.setContentType("application/json;charset=utf-8");
                            PrintWriter out = httpServletResponse.getWriter();
                            out.write(new Gson().toJson(resp));
                            out.flush();
                            out.close();
                }).successHandler(
                        (httpServletRequest, httpServletResponse, authentication) -> {
                            Resp resp = adminService.login(httpServletRequest);
                            httpServletResponse.setContentType("application/json;charset=utf-8");
                            PrintWriter out = httpServletResponse.getWriter();
                            out.write(new Gson().toJson(resp));
                            out.flush();
                            out.close();
                }).and()
                .logout().logoutUrl("/user/logout")
                .logoutSuccessHandler(
                        (httpServletRequest, httpServletResponse, authentication) -> {
                        httpServletRequest.getSession().removeAttribute("user");
                        httpServletResponse.setContentType("application/json;charset=utf-8");
                        PrintWriter out = httpServletResponse.getWriter();
                        out.write(new Gson().toJson(RespUtil.successResp()));
                        out.flush();
                        out.close();
                })
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling();
        http.addFilterAfter(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                .exceptionHandling().accessDeniedPage("/auth/accessDenied");
    }


}
