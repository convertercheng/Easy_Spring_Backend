package com.qhieco.web.controller;

import com.qhieco.constant.Status;
import com.qhieco.request.web.FeeRequest;
import com.qhieco.request.web.FeeRuleAddParkingRequest;
import com.qhieco.request.web.FeeRuleAddReserveRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.webservice.FeeRuleService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/27 下午2:34
 * <p>
 * 类说明：
 *     费用规则的controller层
 */
@RestController
@RequestMapping("fee/rule")
@Slf4j
public class FeeRuleWeb {

    @Autowired
    private FeeRuleService feeRuleService;

    @PostMapping("add/parking")
    public Resp addNewParkingFee(FeeRuleAddParkingRequest feeRuleAddParkingRequest) {
        Resp checkResp = ParamCheck.check(feeRuleAddParkingRequest, "name","type");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.EMPTY_FEE_RULE_PARKING_ADD_PARAM.getCode());
        }
        return feeRuleService.addParkingFee(feeRuleAddParkingRequest);
    }

    @PostMapping("add/reserve")
    public Resp addNewReserveFee(FeeRuleAddReserveRequest feeRuleAddReserveRequest) {
        Resp checkResp = ParamCheck.check(feeRuleAddReserveRequest, "name", "advanceTimeList", "feeList");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.EMPTY_FEE_RULE_RESERVE_ADD_PARAM.getCode());
        }
        return feeRuleService.addReserveFee(feeRuleAddReserveRequest);
    }

    @PostMapping("parking/pageable")
    public Resp queryParking(FeeRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return feeRuleService.queryParkingFee(request);
    }

    @PostMapping("reserve/pageable")
    public Resp queryReserve(FeeRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return feeRuleService.queryReserveFee(request);
    }
}
