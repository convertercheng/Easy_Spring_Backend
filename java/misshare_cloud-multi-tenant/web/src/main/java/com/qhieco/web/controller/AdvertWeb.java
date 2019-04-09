package com.qhieco.web.controller;

import com.qhieco.commonentity.Advert;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AdvertDeleteRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AdvertService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/6 10:30
 * <p>
 * 类说明：
 * 启动广告页web
 */
@Controller
@RequestMapping(value = "advert")
@Slf4j
public class AdvertWeb {

    @Autowired
    private AdvertService advertService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    /**
     * 保存和更新广告页数据
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "saveOrUpdate", method = RequestMethod.POST)
    @ResponseBody
    public Resp saveOrUpdate(HttpServletRequest request) {
        try {
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
            String id = params.getParameter("id");
            List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
            Integer phoneType = Integer.valueOf(params.getParameter("phoneType"));
            Integer countdown = Integer.valueOf(params.getParameter("countdown"));
            Integer jumpable = Integer.valueOf(params.getParameter("jumpable"));
            String href = params.getParameter("href");
            Integer fileId = StringUtils.isEmpty(params.getParameter("fileId")) ? null : Integer.valueOf(params.getParameter("fileId"));

            log.info("保存和更新广告页数据 参数：id = " + id + ", phoneType = " + phoneType + ", countdown = " + countdown +
                    ", jumpable = " + jumpable + ", href= " + href + ", fileId = " + fileId + ", files = " + files);

            if (StringUtils.isEmpty(id) && (files == null || files.size() == 0)) {
                log.error(" 首次保存数据，必须上传图片");
                return RespUtil.errorResp(Status.WebErr.INSERT_ERROR.getCode(), Status.WebErr.INSERT_ERROR.getMsg());
            }

            long now = System.currentTimeMillis();
            String filePath = "";
            String originalName = "";
            if (files != null && files.size() > 0) {
                MultipartFile multipartFile = files.get(0);
                if(multipartFile.getSize() > 2000000){
                    log.error("文件大小超过2M，不能上传，" + multipartFile.getSize());
                    return RespUtil.errorResp(Status.WebErr.FILE_TOOBIG_ERROR.getCode(), Status.WebErr.FILE_TOOBIG_ERROR.getMsg());
                }

                originalName = multipartFile.getOriginalFilename();
                // 获取上传文件后缀,如  .jpg
                String suffix = originalName.substring(originalName.lastIndexOf("."));
                filePath = Constants.DIRECTORY_ADVERT + now + suffix;
                File file = new File(configurationFiles.getWebUploadPath(), filePath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                multipartFile.transferTo(file);

                if (fileId != null) {
                    String originalFilePath = advertService.queryPathById(fileId);
                    new File(configurationFiles.getWebUploadPath() + File.separator + originalFilePath).delete();
                }
            } else {
                log.error("广告页上传图片为空");
            }
            Advert advert = advertService.saveOrUpdate(id, phoneType, countdown, jumpable, href,
                    fileId, originalName, filePath);
            return RespUtil.successResp(advert);
        } catch (Exception e) {
            log.error(" 保存广告页数据异常，" + e);
        }
        return RespUtil.errorResp(Status.ApiErr.INSERT_ERROR.getCode(), Status.ApiErr.INSERT_ERROR.getMsg());
    }

    /**
     * 根据手机类型查询广告页信息
     *
     * @param phoneType
     * @return
     */
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @ResponseBody
    public Resp query(@RequestParam(value = "phoneType") int phoneType) {
        return RespUtil.successResp(advertService.queryByPhoneType(phoneType));
    }

    /**
     * 删除广告页对象，传入广告页对象id，fileId
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Resp deleteFileAndAdvert(@RequestBody AdvertDeleteRequest request) {
        if (request.getId() == null || request.getFileId() == null) {
            log.error(" 删除广告页对象数据接口，传入参数异常，" + request);
            return RespUtil.errorResp(Status.ApiErr.PARAMS_ERROR.getCode(), Status.ApiErr.PARAMS_ERROR.getMsg());
        }
        advertService.deleteById(request.getId(), request.getFileId());
        return RespUtil.successResp();
    }
}
