package com.qhieco.trafficmanage.service;

import com.qhieco.trafficmanage.entity.request.CancelSignRequest;
import com.qhieco.trafficmanage.entity.request.CarAccessUploadRequest;
import com.qhieco.trafficmanage.entity.request.DelayEnterRequest;
import com.qhieco.trafficmanage.entity.request.EnterRequest;
import com.qhieco.trafficmanage.entity.request.LeaveRequest;
import com.qhieco.trafficmanage.entity.request.LockPayQueryRequest;
import com.qhieco.trafficmanage.entity.request.LockPayRequest;
import com.qhieco.trafficmanage.entity.request.ParklotInfoRequest;
import com.qhieco.trafficmanage.entity.request.ParklotSceneUploadRequest;
import com.qhieco.trafficmanage.entity.request.PayInfoRequest;
import com.qhieco.trafficmanage.entity.request.PhotoRequest;
import com.qhieco.trafficmanage.entity.request.RepayInfoRequest;
import com.qhieco.trafficmanage.entity.request.ReuploadCarInOutPicRequest;
import com.qhieco.trafficmanage.entity.request.SignRequest;
import com.qhieco.trafficmanage.entity.request.UnlockPayRequest;
import com.qhieco.trafficmanage.entity.response.CancelSignResponse;
import com.qhieco.trafficmanage.entity.response.DelayEnterResponse;
import com.qhieco.trafficmanage.entity.response.EnterResponse;
import com.qhieco.trafficmanage.entity.response.LeaveResponse;
import com.qhieco.trafficmanage.entity.response.LockPayQueryResponse;
import com.qhieco.trafficmanage.entity.response.LockPayResponse;
import com.qhieco.trafficmanage.entity.response.ParklotInfoResponse;
import com.qhieco.trafficmanage.entity.response.PayInfoResponse;
import com.qhieco.trafficmanage.entity.response.RepayInfoResponse;
import com.qhieco.trafficmanage.entity.response.ReuploadCarInOutPicResponse;
import com.qhieco.trafficmanage.entity.response.SignResponse;
import com.qhieco.trafficmanage.entity.response.UnlockPayResponse;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
import com.qhieco.trafficmanage.exception.QhieWebException;
import com.qhieco.trafficmanage.service.TrafficManageService;
import com.qhieco.util.RespUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:40
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class TrafficManageServiceImpl implements TrafficManageService {

    private static final Integer SUCCESS_RESP = 0;

    @Autowired
    TrafficManageHelper manageHelper;

    @Override
    public Resp<ParklotInfoResponse> parklotInfo(ParklotInfoRequest request) {
        ParklotInfoResponse response = manageHelper.sendMessage(request, ParklotInfoResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp<EnterResponse> carEnter(EnterRequest request) {
        EnterResponse response = manageHelper.sendMessage(request, EnterResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getRCCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getRCCLJGMS());
        }
    }

    @Override
    public Resp<LeaveResponse> carLeave(LeaveRequest request) {
        LeaveResponse response = manageHelper.sendMessage(request, LeaveResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCCCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCCCLJGMS());
        }
    }

    @Override
    public DelayEnterResponse delayEnter(DelayEnterRequest request) {
        if (SUCCESS_RESP.equals(Integer.valueOf(request.getRCCLJG()))){

            /**
             * 平台返回成功回调的处理逻辑
             */
            DelayEnterResponse response = new DelayEnterResponse();
            response.setFHJG("0");
            response.setCSID(request.getCSID());
            response.setJYLX(request.getJYLX());
            response.setRCCSPTLS(request.getRCCSPTLS());
            return response;
        }else {

            /**
            平台返回失败的处理逻辑
             */
            return null;
        }
    }

    @Override
    public Resp photo(PhotoRequest request) {
        return null;
    }

    @Override
    public Resp payInfo(PayInfoRequest request) {
        //如果付款结果为 0,则补扣付款金额，付款信息必填
        //如果付款结果为 1,则代扣失败渠道必填
        System.out.println(request.getFKJG());
        if(Integer.valueOf(request.getFKJG()).equals(0)){
            if(request.getFKJE() == null || request.getFKXX() == null){
                throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
            }
        }else{
            if(request.getDKSBQD() == null){
                throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
            }
        }
        PayInfoResponse response = manageHelper.sendMessage(request,PayInfoResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp repayInfo(RepayInfoRequest request) {
        //如果付款结果为 0,则补扣付款金额，付款信息必填
        if(Integer.valueOf(request.getFKJG()).equals(0)){
            if(request.getBKFKSJ() == null || request.getFKXX() == null){
                throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);

            }
        }
        RepayInfoResponse response = manageHelper.sendMessage(request,RepayInfoResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }

    }

    @Override
    public Resp lockPayQuery(LockPayQueryRequest request) {
        LockPayQueryResponse response = manageHelper.sendMessage(request,LockPayQueryResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp lockPay(LockPayRequest request) {
        LockPayResponse response = manageHelper.sendMessage(request,LockPayResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp unlockPay(UnlockPayRequest request) {
        UnlockPayResponse response = manageHelper.sendMessage(request,UnlockPayResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp signin(SignRequest request) {

        SignResponse response = manageHelper.sendMessage(request,SignResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp cancelSignin(CancelSignRequest request) {
        CancelSignResponse response = manageHelper.sendMessage(request,CancelSignResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }

    }

    @Override
    public Resp reuploadCarInOutPic(ReuploadCarInOutPicRequest request){
        ReuploadCarInOutPicResponse response = manageHelper.sendMessage(request,ReuploadCarInOutPicResponse.class);
        if(SUCCESS_RESP.equals(Integer.valueOf(response.getCLJG()))){
            return RespUtil.successResp(response);
        }else{
            return RespUtil.errorResp(Status.WebErr.TRAFFIC_MANAGE_EEROR.getCode(),response.getCLJGMS());
        }
    }

    @Override
    public Resp<String> uploadParkScene(ParklotSceneUploadRequest request){
        return manageHelper.uploadParklotInfo(request);
    }

    @Override
    public Resp<String> uploadCarAccessInfo(CarAccessUploadRequest request){
        return manageHelper.uploadCarAccessInfo(request);
    }
}
