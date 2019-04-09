package com.qhieco.apiwrite.web;

import com.qhieco.apiservice.OrderChargingService;
import com.qhieco.apiservice.ReserveService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ConfirmRequest;
import com.qhieco.request.api.OrderIdRequest;
import com.qhieco.request.api.OrderParkingRequest;
import com.qhieco.request.api.SaveSelectRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/5 14:29
 * <p>
 * 类说明：
 * 卡卷接口层
 */

@RestController
@RequestMapping("/reserve")
public class ReserveWeb {


    @Autowired
    private ReserveService reserveService;

    @Autowired
    private OrderChargingService orderChargingService;

    @PostMapping("/confirm")
    public Resp confirm(@RequestBody ConfirmRequest confirmRequest) {
        Resp resp = ParamCheck.check(confirmRequest, "user_id", "parklot_id", "plate_id", "share_startTime", "share_endTime", "end_time");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return reserveService.confirm(confirmRequest);
    }

    @PostMapping("/cancel")
    public Resp cancel(@RequestBody OrderIdRequest orderIdRequest) {
        Resp resp = ParamCheck.check(orderIdRequest,  "order_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return reserveService.cancel(orderIdRequest);
    }

    @PostMapping("/saveSelect")
    public Resp saveSelect(@RequestBody SaveSelectRequest saveSelectRequest) {
        Resp resp = ParamCheck.check(saveSelectRequest,  "mobileUserId","parklotId","startTime","endTime");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return reserveService.saveSelect(saveSelectRequest);
    }
    @PostMapping("/saveOrderParking")
    public Resp saveOrderParking(@RequestBody OrderParkingRequest orderParkingRequest){
        Resp resp = ParamCheck.check(orderParkingRequest,  "reserveId");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return reserveService.saveOrderParking(orderParkingRequest);
    }

    @PostMapping("/process")
    public Resp process(@RequestBody OrderParkingRequest orderParkingRequest)throws Exception{
        Resp resp = ParamCheck.check(orderParkingRequest,  "parkLotId","parkLoctId","tag");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return orderChargingService.process(orderParkingRequest.getTag(),orderParkingRequest.getParkLotId(),orderParkingRequest.getParkLoctId());
    }

    @GetMapping("getFee")
    public Resp getFee(){
        return orderChargingService.getFee();
    }

}
