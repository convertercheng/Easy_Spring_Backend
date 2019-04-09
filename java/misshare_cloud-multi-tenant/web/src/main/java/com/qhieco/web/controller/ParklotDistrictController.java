package com.qhieco.web.controller;


import com.qhieco.commonentity.ParklotDistrict;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotDistrictRequest;
import com.qhieco.request.web.ParklotInfoRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.ParklotDistrictService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 18-7-06 上午10:59
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("district")
@Slf4j
public class  ParklotDistrictController {

    @Autowired
    private ParklotDistrictService parklotDistrictService;

    /**
     * 分页显示车位区域列表
     * @param parklotDistrictRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/pageable")
    public Resp pageable(ParklotDistrictRequest parklotDistrictRequest)throws Exception{
        Resp checkResp = ParamCheck.check(parklotDistrictRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return parklotDistrictService.query(parklotDistrictRequest);
    }

    /**
     * 导出车位区域列表信息
     * @param response
     * @param parklotDistrictRequest
     * @throws Exception
     */
    @PostMapping("/excel")
    public void excel(HttpServletResponse response, ParklotDistrictRequest parklotDistrictRequest)throws Exception{
        // 下载文件的默认名称
        try {
            parklotDistrictService.excel(parklotDistrictRequest, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("车位区域列表" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * 车位区域详情
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id)throws Exception{
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return parklotDistrictService.getParklotDistrictInfo(id);
    }

    /**
     * 新增或修改车位区域信息
     * @param parklotDistrict
     * @return
     * @throws Exception
     */
    @PostMapping("/addNewParklotDistrict")
    public Resp addNewParklotDistrict(ParklotDistrict parklotDistrict)throws Exception {
        Resp checkResp = ParamCheck.check(parklotDistrict, "districtName", "parklotId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.EMPTY_PARKLOT_ADD_PARAM.getCode());
        }
        return parklotDistrictService.saveUpdate(parklotDistrict);
    }

    /**
     * 批量或单个生成车位区域二维码
     * @param httpServletRequest
     * @param httpServletResponse
     * @param data
     * @throws Exception
     */
    @GetMapping(value="/downloadAllQr")
    public void  downloadAllQr(HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse, @RequestParam("data") String data)throws Exception{
        parklotDistrictService.downloadAllQr(httpServletRequest,httpServletResponse,data);

    }

    /**
     * 根据车场ID查询区域列表
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping("/getParklotDistrictByParklotList/{id}")
    public Resp getParklotDistrictByParklotList(@PathVariable Integer id)throws Exception {
        if(id == null) {
            throw new QhieWebException(Status.WebErr.EMPTY_INPUT_PARAM);
        }
        return parklotDistrictService.getParklotDistrictByParklotList(id);
    }

}
