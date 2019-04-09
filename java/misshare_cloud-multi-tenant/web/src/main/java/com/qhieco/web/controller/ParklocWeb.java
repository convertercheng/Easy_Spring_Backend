package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.ParklocService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-27 上午10:59
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("parkloc")
@Slf4j
public class ParklocWeb {

    @Autowired
    ParklocService parklocService;

    @PostMapping("pageable")
    public Resp all(ParklocRequest parklocRequest){
        Resp checkResp = ParamCheck.check(parklocRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return parklocService.query(parklocRequest);
    }

    @PostMapping("excel")
    public void excel(HttpServletResponse response, ParklocRequest parklocRequest){
        if (ExcelUtil.paramCount(parklocRequest) == Constants.EMPTY_CAPACITY) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            parklocService.excel(parklocRequest, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("车位列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @PostMapping("add")
    public Resp addNewParkloc(ParklocAddRequest parklocAddRequest) {
        Resp checkResp = ParamCheck.check(parklocAddRequest, "parklotId", "userId", "number", "parklocId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return parklocService.addParkloc(parklocAddRequest);
    }

    @PostMapping("add/batch")
    public Resp batchAddParkloc(ParklocBatchAddRequest parklocBatchAddRequest) {
        Resp checkResp = ParamCheck.check(parklocBatchAddRequest, "parklotId", "userId", "numbers");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return parklocService.batchAddParkloc(parklocBatchAddRequest);
    }

    @PostMapping("edit")
    public Resp editParkloc(ParklocEditRequest parklocEditRequest) {
        Resp checkResp = ParamCheck.check(parklocEditRequest, "parklocId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return parklocService.editParkloc(parklocEditRequest);
    }

    @PostMapping("publish/info")
    public Resp publishInfo(ParklocPublishInfoRequest parklocPublishInfoRequest) {
        Resp checkResp = ParamCheck.check(parklocPublishInfoRequest, "parklocId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return parklocService.publishInfo(parklocPublishInfoRequest);
    }

}
