package com.qhieco.web.controller;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.CouponRequest;
import com.qhieco.request.web.OrderRequest;
import com.qhieco.request.web.ParamsRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.ParamsService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/8 17:04
 * <p>
 * 类说明：
 * 参数模块接口类
 */
@RestController
@RequestMapping("/params")
@Slf4j
public class ParamsWeb {

    @Autowired
    private ParamsService paramsService;

    /**
     * 参数值列表分页
     * @param paramsRequest
     * @return
     */
    @PostMapping(value = "pageable")
    public Resp query(ParamsRequest paramsRequest) {
        Resp checkResp = ParamCheck.check(paramsRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return paramsService.query(paramsRequest);
    }

    /**
     * 查询参数值详情
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    public Resp one(@PathVariable Integer id) {
        return paramsService.one(id);
    }

    /**
     * 新增或修改参数值
     * @param paramsRequest
     * @return
     */
    @PostMapping(value = "/saveUpdate")
    public Resp saveUpdate(ParamsRequest paramsRequest) {
        Resp checkResp = ParamCheck.check(paramsRequest, "qhkey", "qhvalue", "level");
        return paramsService.saveUpdate(paramsRequest);
    }
}
