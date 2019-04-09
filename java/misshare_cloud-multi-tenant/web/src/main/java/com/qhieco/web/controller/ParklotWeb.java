package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.TenantContext;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.ParklotService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/21 下午2:59
 * <p>
 * 类说明：
 *     有关停车区的web控制层
 */
@RestController
@RequestMapping("parklot")
@Slf4j
public class ParklotWeb {

    @Autowired
    private ParklotService parklotService;

    @Autowired
    private FeeRuleService feeRuleService;

    @PostMapping("add")
    public Resp addNewParklot(ParklotInfoRequest parklotInfoRequest) {
        Resp checkResp = ParamCheck.check(parklotInfoRequest, "areaId", "address", "name", "totalAmount",
                "contactName", "contactPhone", "signedAmount", "latLng", "naviLatLng", "type", "kind", "state",
                "parklotId","chargeType");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.EMPTY_PARKLOT_ADD_PARAM.getCode());
        }
        return parklotService.addNewParklot(parklotInfoRequest);
    }

    @PostMapping("pic/remove")
    public Resp removeParklotFile(ParklotFileRemoveRequest parklotFileRemoveRequest) {
        Resp checkResp = ParamCheck.check(parklotFileRemoveRequest, "parklotId", "fileId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.PARAM_ERROR.getCode());
        }
        return parklotService.deleteFile(parklotFileRemoveRequest);
    }
    
    @PostMapping("pageable")
    public Resp all(ParklotRequest parklotRequest){
        Resp checkResp = ParamCheck.check(parklotRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return parklotService.query(parklotRequest);
    }

    @PostMapping("bind/fee/rule")
    public Resp bindFeeRule(BindFeeRuleRequest bindFeeRuleRequest) {
        Resp checkResp = ParamCheck.check(bindFeeRuleRequest, "feeRuleId", "parklotId", "type");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.EMPTY_FEE_RULE_BIND.getCode());
        }
        return feeRuleService.bindFeeRule(bindFeeRuleRequest);
    }


    @GetMapping(value = "{id}")
    public Resp one(@PathVariable Integer id){
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return parklotService.findOne(id);
    }

    @GetMapping(value = "admin/{id}")
    public Resp getParklotAdmin(@PathVariable Integer id) {
       if (null == id) {
           throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
       }
        return parklotService.getAdmin(id);
    }


    @RequestMapping("excel")
    public void excel(HttpServletResponse response, ParklotRequest parklotRequest){
        if (ExcelUtil.paramCount(parklotRequest.getName(),parklotRequest.getType()) == 0) {
            throw new QhieWebException(Status.WebErr.EMPTY_EXCEL_PARAM);
        }
        // 下载文件的默认名称
        try {
            parklotService.excel(parklotRequest, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("停车场列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @GetMapping(value="/downloadAllQr")
    public void  downloadAllQr(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse, @RequestParam("data") String data)throws Exception{
        parklotService.downloadAllQr(httpServletRequest,httpServletResponse,data);

    }
}
