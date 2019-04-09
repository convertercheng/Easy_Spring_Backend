package com.qhieco.web.controller;

import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Status;
import com.qhieco.request.web.UpdateRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.UpdateService;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-3-23 下午2:38
 * <p>
 * 类说明：
 * ${description}
 */
@Controller
@RequestMapping("/update")
@Slf4j
public class UpdateWeb {
    @Autowired
    UpdateService updateService;

    @Autowired
    ConfigurationFiles configurationFiles;


    @ResponseBody
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Resp uploadAPK(@RequestParam("file") MultipartFile file) {
        try {

            if (file == null || file.isEmpty()) {
                log.error("empty file.");
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }

            String dir = configurationFiles.getWebUploadPath().concat("app/apk");
            String filename = file.getOriginalFilename();
            File saveFile = new File(dir, filename);
            File parentFile = saveFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.transferTo(saveFile);
            return RespUtil.successResp();
        } catch (Exception e) {
            log.error("system error." + e);
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Resp save(@ModelAttribute UpdateRequest updateRequest) {
            Resp resp = ParamCheck.check(updateRequest,  "vcode","type");
            if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }
            return updateService.saveOrUpdate(updateRequest);
    }


}
