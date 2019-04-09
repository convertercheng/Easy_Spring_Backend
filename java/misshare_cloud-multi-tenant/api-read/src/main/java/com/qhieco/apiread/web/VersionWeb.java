package com.qhieco.apiread.web;

import com.qhieco.apiservice.VersionService;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.request.api.UpdateRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 下午8:46
 * <p>
 * 类说明：
 * version controller
 */
@RestController
@RequestMapping("version")
@Slf4j
public class VersionWeb {

    @Autowired
    private VersionService versionService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @PostMapping("/updateInfo")
    public Resp updateInfo(@RequestBody UpdateRequest updateRequest) {
        return versionService.updateInfo(updateRequest);
    }


    @GetMapping("/download")
    public void download(@Param("type") int type, HttpServletResponse httpServletResponse) {
        String filePath = null;
        if (0 == type) {
            filePath = configurationFiles.getWebUploadPath() + File.separator + "app/apk/app-release.apk";
        }
        log.info("下载安装包：filePath=" + filePath);
        File file = new File(filePath);
        httpServletResponse.setHeader("content-type", "application/octet-stream");
        httpServletResponse.setContentType("application/octet-stream");
        httpServletResponse.setHeader("Cache-Control", "no-transform, public, max-age=86400");
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=".concat(file.getName()));
        httpServletResponse.setContentLengthLong(file.length());
        try {
            FileUtils.download(file, httpServletResponse);
        } catch (IOException e) {
            httpServletResponse.setStatus(404);
            log.error("下载安装包异常：" + e);
        }
    }
}
