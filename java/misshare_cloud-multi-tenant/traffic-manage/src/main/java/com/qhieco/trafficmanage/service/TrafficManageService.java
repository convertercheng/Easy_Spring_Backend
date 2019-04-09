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
import com.qhieco.response.Resp;
import com.qhieco.trafficmanage.entity.response.DelayEnterResponse;
import com.qhieco.trafficmanage.entity.response.EnterResponse;
import com.qhieco.trafficmanage.entity.response.LeaveResponse;
import com.qhieco.trafficmanage.entity.response.ParklotInfoResponse;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-6-12 上午10:33
 * <p>
 * 类说明：
 * ${description}
 */
public interface TrafficManageService {

    Resp<ParklotInfoResponse> parklotInfo(ParklotInfoRequest request);

    Resp<EnterResponse> carEnter(EnterRequest request);

    Resp<LeaveResponse> carLeave(LeaveRequest request);

    DelayEnterResponse delayEnter(DelayEnterRequest request);

    Resp photo(PhotoRequest request);

    Resp payInfo(PayInfoRequest request);

    Resp repayInfo(RepayInfoRequest request);

    Resp lockPayQuery(LockPayQueryRequest request);

    Resp lockPay(LockPayRequest request);

    Resp unlockPay(UnlockPayRequest request);

    Resp signin(SignRequest request);

    Resp cancelSignin(CancelSignRequest request);

    Resp reuploadCarInOutPic(ReuploadCarInOutPicRequest request);

    Resp<String> uploadParkScene(ParklotSceneUploadRequest request);

    Resp<String> uploadCarAccessInfo(CarAccessUploadRequest request);

}
