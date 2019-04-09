package com.qhieco.web.controller;

import com.qhieco.commonentity.Message;
import com.qhieco.commonentity.Tag;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.TagService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 18-4-6 下午2:38
 * <p>
 * 类说明：
 * 标签控制层
 */
@Controller
@RequestMapping("/tag")
@Slf4j
public class TagWeb {
    @Autowired
    TagService tagService;

    @Autowired
    ConfigurationFiles configurationFiles;


    @ResponseBody
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public Resp save(@ModelAttribute Tag tag) {
        Resp resp = ParamCheck.check(tag, "name", "userType");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        return tagService.saveOrUpdate(tag);
    }

    @ResponseBody
    @RequestMapping(value = "/pageable", method = RequestMethod.POST)
    public Resp all(@ModelAttribute TagPageableRequest tagPageableRequest) {
        Resp checkResp = ParamCheck.check(tagPageableRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return tagService.pageable(tagPageableRequest);
    }

    @ResponseBody
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public Resp delete(@RequestBody IdRequest idRequest) {
        if (null == idRequest.getId()) {
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        return tagService.delete(idRequest.getId());
    }


    @ResponseBody
    @RequestMapping(value = "/all", method = RequestMethod.POST)
    public Resp all() {
        return tagService.all();
    }

    @ResponseBody
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, TagPageableRequest tagPageableRequest) {
        if (ExcelUtil.paramCount(tagPageableRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            tagService.excel(tagPageableRequest, response.getOutputStream(), Tag.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("标签" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @ResponseBody
    @RequestMapping(value = "{id}")
    public Resp one(@PathVariable Integer id) {
        if (null == id) {
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        return tagService.one(id);
    }

    @ResponseBody
    @RequestMapping(value = "message/pageable", method = RequestMethod.POST)
    public Resp messagePageable(@ModelAttribute MessagePageableRequest messagePageableRequest) {
        Resp checkResp = ParamCheck.check(messagePageableRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return tagService.messagePageable(messagePageableRequest);
    }

    @ResponseBody
    @RequestMapping(value = "message/excel")
    public void messageExcel(HttpServletResponse response, MessagePageableRequest messagePageableRequest) {
        if (ExcelUtil.paramCount(messagePageableRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            tagService.messageExcel(messagePageableRequest, response.getOutputStream(), Message.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("消息" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    @ResponseBody
    @RequestMapping(value = "message/upload", method = RequestMethod.POST)
    public Resp uploadPic(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                log.error("empty file.");
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }

            String dir = configurationFiles.getWebUploadPath().concat("img/message");
            String filename = file.getOriginalFilename();
            File saveFile = new File(dir, filename);
            File parentFile = saveFile.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.transferTo(saveFile);
            com.qhieco.commonentity.File fileData = new com.qhieco.commonentity.File();
            fileData.setName(filename);
            fileData.setPath("img/message/" + filename);
            fileData.setCreateTime(System.currentTimeMillis());
            fileData.setState(Status.Common.VALID.getInt());
            fileData.setIntro(filename);
            return tagService.saveFile(fileData);
        } catch (Exception e) {
            log.error("system error." + e);
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
    }


    @ResponseBody
    @RequestMapping(value = "message/sendJpushByUserId", method = RequestMethod.POST)
    public Resp sendJpushByUserId(@ModelAttribute SendJpushRequest sendJpushRequest) {
        Resp checkResp = ParamCheck.check(sendJpushRequest, "webUserId", "userIds", "type", "title", "content");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return tagService.sendJpushByUserId(sendJpushRequest);
    }


    @ResponseBody
    @RequestMapping(value = "message/sendJpushByTagId", method = RequestMethod.POST)
    public Resp sendJpushByTagId(@ModelAttribute SendJpushRequest sendJpushRequest) {
        Resp checkResp = ParamCheck.check(sendJpushRequest, "webUserId", "tagIds", "type", "title", "content");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return tagService.sendJpushByTagId(sendJpushRequest);
    }

    /**
     * 保存和更新手动标签
     *
     * @return
     */
    @PostMapping(value = "manual/saveOrUpdate")
    @ResponseBody
    public Resp saveOrUpdateManualTag(ManualTagSaveRequest request) {
        log.info("保存和更新手动标签参数：" + request);
        if (StringUtils.isEmpty(request.getTagName()) || request.getUserIds() == null || request.getUserIds().size() == 0) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        try {
            return tagService.saveOrUpdateManualTag(request);
        } catch (Exception e) {
            log.error("保存和更新手动标签 异常：" + e);
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
    }

    /**
     * 手动标签用户列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "manual/user/list")
    @ResponseBody
    public Resp queryUserList(TagUserRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return RespUtil.successResp(tagService.queryUserList(request));
    }

    @PostMapping(value = "manual/info")
    @ResponseBody
    public Resp queryManualTagInfo(@Param("tagId") Integer tagId) {
        if (StringUtils.isEmpty(tagId)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return RespUtil.successResp(tagService.queryManualTagInfoByTagId(tagId));
    }
}
