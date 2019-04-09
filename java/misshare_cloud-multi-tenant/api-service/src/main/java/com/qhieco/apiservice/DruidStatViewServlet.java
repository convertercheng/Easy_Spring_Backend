package com.qhieco.apiservice;

import com.alibaba.druid.support.http.StatViewServlet;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/5/30 上午10:25
 * <p>
 * 类说明：
 *     StatViewServlet
 */
@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/druid/*",
            initParams = {
                    @WebInitParam(name = "loginUsername", value="admin"),
                    @WebInitParam(name = "laoginPassword", value = "admin"),
                    @WebInitParam(name = "resetEnable", value = "false")
            })
public class DruidStatViewServlet extends StatViewServlet {
}
