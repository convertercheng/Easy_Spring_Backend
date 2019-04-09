package com.qhieco.apiread.web;

import com.qhieco.apiservice.PlateService;
import com.qhieco.apiservice.WxAuthorService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Status;
import com.qhieco.request.api.ParkingInfoRequest;
import com.qhieco.request.api.PlateGetRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.PlateListRepData;
import com.qhieco.response.data.api.PlateRepData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/2/7 18:26
 * <p>
 * 类说明：
 * 关于车牌号的控制类
 */
@RestController
@RequestMapping("/plate")
@Slf4j
public class PlateWeb {

    @Autowired
    private PlateService plateService;

    @Autowired
    private WxAuthorService wxAuthorService;

    @Autowired
    private ConfigurationFiles configuration;

    /**
     * 查询车牌号列表
     */
    @PostMapping(value = "/get")
    public Resp plateGet(@RequestBody PlateGetRequest request) {
        if (CommonUtil.isTimeStampInValid(request.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (request.getUser_id() == null || request.getPage_num() == null || request.getPage_num() < 0) {
            throw new QhieException(Status.ApiErr.PARAMS_PLATE_GET);
        }
        List<PlateRepData> repDatas = plateService.queryPlateListByUserId(request.getUser_id(), request.getPage_num());
        PlateListRepData listRepData = new PlateListRepData();
        listRepData.setPlates(repDatas);
        return RespUtil.successResp(listRepData);
    }


    /**
     * 查询停车订单费用信息,判断是否是已预约还是未预约的订单，
     *
     * @param request
     * @return
     */
    @PostMapping(value = "parking/redirect")
    public ModelAndView parkingInfoRedirect(ParkingInfoRequest request) {
        Resp checkResp = ParamCheck.check(request, "unionId", "plateNo", "parklotId", "openId", "timestamp");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        log.info("扫码支付  查询停车订单信息重定向到不同的页面  参数 request = " + request);
        String url = "";
        try {
            url = configuration.getMpUrlPrefix() + wxAuthorService.queryParkingInfoRedirect(request.getPlateNo(), request.getParklotId(),
                    request.getUnionId(), request.getOpenId());
        } catch (Exception e) {
            log.error("查询停车订单信息重定向 异常：" + e);
            url = configuration.getMpUrlPrefix() + "ihomefast/error/";
        }
        log.info("扫码支付跳转路径：" + url);
        return new ModelAndView("redirect:" + url);
    }

    /**
     * 查询扫码支付停车订单费用信息
     *
     * @param request
     * @return
     */
    @PostMapping(value = "parking")
    public Resp parkingInfo(@RequestBody ParkingInfoRequest request) {
        log.info("扫码支付  查询停车订单信息参数 request = " + request);
        Resp checkResp = ParamCheck.check(request, "unionId", "plateNo", "parklotId", "openId", "timestamp");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new QhieException(Status.ApiErr.PARAMS_ERROR);
        }
        return RespUtil.successResp(wxAuthorService.queryParkingInfo(request.getPlateNo(), request.getParklotId(),
                request.getUnionId(), request.getOpenId()));
    }

}