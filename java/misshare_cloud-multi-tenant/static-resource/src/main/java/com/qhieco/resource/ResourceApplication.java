package com.qhieco.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:39
 *          <p>
 *          类说明：
 *          启动类
 */

@SpringBootApplication
@ServletComponentScan
@EnableScheduling
public class ResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ResourceApplication.class, args);
    }


}
