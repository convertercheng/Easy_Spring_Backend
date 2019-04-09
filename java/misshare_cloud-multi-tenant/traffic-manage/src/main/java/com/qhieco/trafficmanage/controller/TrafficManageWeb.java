package com.qhieco.trafficmanage.controller;

import com.google.gson.Gson;
import com.qhieco.constant.Status;
import com.qhieco.response.Resp;
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
import com.qhieco.trafficmanage.entity.response.ReuploadCarInOutPicResponse;
import com.qhieco.trafficmanage.entity.response.SignResponse;
import com.qhieco.trafficmanage.entity.response.UnlockPayResponse;
import com.qhieco.trafficmanage.exception.ParamException;
import com.qhieco.trafficmanage.exception.QhieWebException;
import com.qhieco.trafficmanage.service.TrafficManageService;
import com.qhieco.util.ParamCheck;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 下午2:11
 * <p>
 * 类说明：
 * ${description}
 */
@RestController
@RequestMapping("/traffic/manage")
@Slf4j
public class TrafficManageWeb {

    @Autowired
    TrafficManageService trafficManageService;

    private Gson gson = new Gson();

    /**
     * 停车场管理系统向平台报送停车场维护信息
     * @param request
     * @return
     */
    @PostMapping(value = "/parklotInfo")
    public Resp<ParklotInfoResponse> parklotInfo(@RequestBody JSONObject request){
        ParklotInfoRequest parklotInfoRequest = gson.fromJson(request.toString(),ParklotInfoRequest.class);
        Resp checkResp = ParamCheck.check(parklotInfoRequest, "TCCID","TCCMC","JWDBZ","TCCJD","TCCWD","TCCCWS",
                "KRNCWS","GXSJ","XXDZ","JCKXX");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return trafficManageService.parklotInfo(parklotInfoRequest);
    }

    /**
     * 停车场车辆入场信息上报
     * @param request
     * @return
     */
    @PostMapping(value = "/enter")
    public Resp<EnterResponse> carEnter(@RequestBody JSONObject request){
        EnterRequest enterRequest = gson.fromJson(request.toString(),EnterRequest.class);
        Resp checkResp = ParamCheck.check(enterRequest, "TCCID","TCCMC","TCCJKID","JCKBH","JCKMC","CPHM","CPLX",
                "TGSJ","TPIDS","TPCSCC","SFWC","BCBZ","TCCCWS","ZCCLS","SYCWS","YKBZ","DKPT","HTTPHDDZ");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return trafficManageService.carEnter(enterRequest);
    }

    /**
     * 此接口为延迟提交情况下，管理平台调用的回调接口，Service逻辑暂时空缺
     * @param request
     * @return
     */
    @PostMapping(value = "/enter/delay")
    public DelayEnterResponse delayEnter(@RequestBody JSONObject request){
        DelayEnterRequest delayEnterRequest = gson.fromJson(request.toString(),DelayEnterRequest.class);
        return trafficManageService.delayEnter(delayEnterRequest);
    }

    /**
     * 车辆离场时信息上报
     * @param request
     * @return
     */
    @PostMapping(value = "/leave")
    public Resp<LeaveResponse> carLeave(@RequestBody JSONObject request){
        LeaveRequest leaveRequest = gson.fromJson(request.toString(),LeaveRequest.class);
        Resp checkResp = ParamCheck.check(leaveRequest, "TCCID","TCCMC","TCCCKID","JCKBH","JCKMC","CPHM","CPLX",
                "TGSJ","TPIDS","TPCSCC","SFWC","BCBZ","TCCCWS","ZCCLS","SYCWS","RCSJ","TCSC","YKBZ");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return trafficManageService.carLeave(leaveRequest);
    }

    /**
     * 停车场管理系统向平台报送支付账务明细
     * @param request
     * @return
     */
    @PostMapping(value = "/payInfoUpload")
    public Resp<PayInfoResponse> payInfoUpload(@RequestBody JSONObject request){
        PayInfoRequest payInfoRequest = gson.fromJson(request.toString(),PayInfoRequest.class);
        Resp checkResp = ParamCheck.check(payInfoRequest,"CCCSPTLS","FKSJ","FKJG");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return trafficManageService.payInfo(payInfoRequest);
    }

    /**
     * 停车场管理系统向平台补传支付账务明细
     * @param request
     * @return
     */
    @PostMapping(value = "/repayInfoUpload")
    public Resp<PayInfoResponse> repayInfoUpload(@RequestBody JSONObject request){
        RepayInfoRequest repayInfoRequest = gson.fromJson(request.toString(),RepayInfoRequest.class);
        Resp checkResp = ParamCheck.check(repayInfoRequest,"CCCSPTLS","BKFKSJ","FKJG");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return trafficManageService.repayInfo(repayInfoRequest);
    }

    /**
     * 停车场管理系统向平台查询代扣锁定情况
     * @param request
     * @return
     */
    @PostMapping(value = "/lockpayproxyquery")
    public Resp<LockPayQueryResponse> lockPayQuery(@RequestBody JSONObject request){
        LockPayQueryRequest lockPayQueryRequest = gson.fromJson(request.toString(),LockPayQueryRequest.class);
        Resp checkResp = ParamCheck.check(lockPayQueryRequest,"PTID","SJH");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.lockPayQuery(lockPayQueryRequest);
    }
    /**
     * 停车场管理系统调用平台进行代扣锁定
     * @param request
     * @return
     */
    @PostMapping(value = "/lockpayproxy")
    public Resp<LockPayResponse> lockPay(@RequestBody JSONObject request){
        LockPayRequest lockPayRequest = gson.fromJson(request.toString(),LockPayRequest.class);
        Resp checkResp = ParamCheck.check(lockPayRequest,"PTID","SJH");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.lockPay(lockPayRequest);
    }
    /**
     * 停车场管理系统调用平台进行代扣解锁
     * @param request
     * @return
     */
    @PostMapping(value = "/unlockpayproxy")
    public Resp<UnlockPayResponse> unlockPay(@RequestBody JSONObject request){
        UnlockPayRequest unlockPayRequest = gson.fromJson(request.toString(),UnlockPayRequest.class);
        Resp checkResp = ParamCheck.check(unlockPayRequest,"PTID","SJH");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.unlockPay(unlockPayRequest);
    }
    /**
     * 平台调用停车场管理系统通知用户签约情况
     * @param request
     * @return
     */
    @PostMapping(value = "/sign")
    public Resp<SignResponse> sign(@RequestBody JSONObject request){
        SignRequest signRequest = gson.fromJson(request.toString(),SignRequest.class);
        Resp checkResp = ParamCheck.check(signRequest,"PTQYLS","PTID","SJH","DKPT","DKPTPID","DKPTUID","QYSJ","QYLS");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            System.out.println(checkResp.getError_code());
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.signin(signRequest);
    }
    /**
     * 停车场管理系统调用平台通知用户解约
     * @param request
     * @return
     */
    @PostMapping(value = "cancelsign")
    public Resp<CancelSignResponse> cancelSign(@RequestBody JSONObject request){
        CancelSignRequest cancelSignRequest = gson.fromJson(request.toString(),CancelSignRequest.class);
        Resp checkResp = ParamCheck.check(cancelSignRequest,"PTJYLS","PTID","SJH","DKPT","DKPTPID","DKPTUID","JYSJ","JYLS");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.cancelSignin(cancelSignRequest);
    }

    /**
     * 停车场管理系统向平台补传出入场图片信息
     * @param request
     * @return
     */

    @PostMapping(value = "reuploadcarinoutpic")
    public Resp<ReuploadCarInOutPicResponse> reuploadCarInOutPic(@RequestBody JSONObject request){
        ReuploadCarInOutPicRequest reuploadCarInOutPicRequest = gson.fromJson(request.toString(),ReuploadCarInOutPicRequest.class);
        Resp checkResp = ParamCheck.check(reuploadCarInOutPicRequest,"JCCBZ","JCCCSPTLS","TGSJ","TPIDS","TPCSCC");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.reuploadCarInOutPic(reuploadCarInOutPicRequest);
    }

    /**
     * 上传停车场场景图
     * @param request
     * @return
     */
    @PostMapping(value = "uploadParkScene")
    public Resp<String> uploadParkScene(ParklotSceneUploadRequest request){
        Resp checkResp = ParamCheck.check(request,"sceneImage","TCCID");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.uploadParkScene(request);
    }

    /**
     * 上传车辆入场出场信息
     * @param request
     * @return
     */
    @PostMapping(value = "uploadCarAccessInfo")
    public Resp<String> uploadCarAccessInfo(CarAccessUploadRequest request){
        Resp checkResp = ParamCheck.check(request,"sceneImage","timestamp","monitoringPoints","Lane","imageIndex");
        if(!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp,Status.WebErr.PARAM_ERROR.getCode());
        }
        return trafficManageService.uploadCarAccessInfo(request);
    }
}
