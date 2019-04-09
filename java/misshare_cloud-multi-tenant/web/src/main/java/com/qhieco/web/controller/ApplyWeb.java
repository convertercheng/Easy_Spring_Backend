package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.ApplyParklocData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.ApplyService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午7:51
 * <p>
 * 类说明：
 *     申请工单web
 */
@RestController
@RequestMapping("apply")
@Slf4j
public class ApplyWeb {

    @Autowired
    private ApplyService applyService;

    /**
     * 分页显示申请工单列表
     * @param applyParklocRequest
     * @return
     */
    @PostMapping("pageable")
    public Resp all(ApplyParklocRequest applyParklocRequest){
        Resp checkResp = ParamCheck.check(applyParklocRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return  applyService.query(applyParklocRequest);
    }

    /**
     * 工单列表导出
     * @param response
     * @param applyParklocRequest
     */
    @RequestMapping(value = "/excel")
    public void excel(HttpServletResponse response, ApplyParklocRequest applyParklocRequest){
        if (ExcelUtil.paramCount(applyParklocRequest) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            applyService.execl(applyParklocRequest,response.getOutputStream(), ApplyParklocData.class);
            response.setHeader("Content-Disposition",
                    "attachment;filename="+ new String(("申请工单列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping("parkloc")
    public Resp applyParkloc(ApplyParklocRequest applyParklocRequest) {
        Resp checkResp = ParamCheck.check(applyParklocRequest, "applyId", "webUserId", "state", "message");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return applyService.processParkloc(applyParklocRequest);
    }

}
