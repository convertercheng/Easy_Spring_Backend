package com.qhieco.apiservice;

import com.alibaba.druid.support.http.WebStatFilter;

import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/5/30 上午10:31
 * <p>
 * 类说明：
 *     Druid的StatFiltera
 */
@WebFilter(filterName = "druidWebStatFilter", urlPatterns = "/*",
            initParams = {
                @WebInitParam(name="exclusions", value = "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")
            })
public class DruidStatFilter extends WebStatFilter {
}
