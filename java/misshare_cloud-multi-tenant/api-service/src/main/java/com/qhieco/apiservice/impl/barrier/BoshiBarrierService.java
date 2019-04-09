package com.qhieco.apiservice.impl.barrier;

import com.google.gson.Gson;
import com.qhieco.apiservice.BarrierApiService;
import com.qhieco.barrier.keytop.response.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.response.Resp;
import com.qhieco.util.DateUtils;
import com.qhieco.util.EncryptUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/21 9:42
 * <p>
 * 类说明：
 * ${description}
 */
@Slf4j
@Service
public class BoshiBarrierService {

    @Autowired
    private BarrierApiService barrierApiService;

    @Autowired
    private ConfigurationFiles configurationFiles;

    /**
     * 初始化请求参数
     * @param boostedGoalParkingMap
     * @return
     */
    public Map<String,String> initParams(Map<String,String> boostedGoalParkingMap){
        boostedGoalParkingMap.put("AccessCode",configurationFiles.getBoostedGoalAccessCode());
        boostedGoalParkingMap.put("SignType","SHAONE");
        return boostedGoalParkingMap;
    }

    /**
     * 初始化加密参数
     * @param boostedGoalParkingRequestList
     * @return
     */
    public String initParamsList(List<String> boostedGoalParkingRequestList){
        boostedGoalParkingRequestList.add("AccessCode="+configurationFiles.getBoostedGoalAccessCode());
        boostedGoalParkingRequestList.add("SignType=SHAONE");
        boostedGoalParkingRequestList.add("Secretkey="+configurationFiles.getBoostedGoalSecretKey());
        String sign = EncryptUtil.signature(boostedGoalParkingRequestList);
        return sign;
    }

    /**
     * 根据车牌获取费用接口
     * @param PlateNumber
     * @param CalculatTime
     * @return
     */
    public KeyTopParkingCostRespone getPlateNumberByParkOrderCost(String PlateNumber,Long CalculatTime){
        Map<String,String> boostedGoalParkingMap=new HashMap<String,String>();
        List<String> boostedGoalParkingRequestList=new ArrayList<>();
        boostedGoalParkingRequestList.add("BusinessCode=PF0001");
        boostedGoalParkingRequestList.add("PlateNumber="+PlateNumber);
        boostedGoalParkingRequestList.add("CompanyID="+configurationFiles.getKeyTopCompanyId());
        boostedGoalParkingRequestList.add("CalculatTime="+DateUtils.timeConvertDateStrings(CalculatTime));
        String sign=initParamsList(boostedGoalParkingRequestList);
        initParams(boostedGoalParkingMap);
        boostedGoalParkingMap.put("BusinessCode","PF0001");
        boostedGoalParkingMap.put("PlateNumber",PlateNumber);
        boostedGoalParkingMap.put("CompanyID",configurationFiles.getKeyTopCompanyId());
        boostedGoalParkingMap.put("CalculatTime", DateUtils.timeConvertDateStrings(CalculatTime));
        boostedGoalParkingMap.put("Sign",sign);
        Gson gson = new Gson();
        String json = "data="+gson.toJson(boostedGoalParkingMap);
        String url = configurationFiles.getBoostedGoalParkingUrl();
        ResponseEntity<String> responseEntity=barrierApiService.registeredTemp(json,url, Constants.APPLICATION_FORM);
        KeyTopParkingCostRespone keyTopParkingCostRespone = new Gson().fromJson(responseEntity.getBody(), KeyTopParkingCostRespone.class);
        log.info("解析后keyTopParkingCostRespone：{}",keyTopParkingCostRespone);
        return keyTopParkingCostRespone;
    }

    /**
     * 支付之前再次验证车辆在车场信息
     * @param PlateNumber
     * @param ParkingID
     * @return
     */
    public boolean validateParkingLotInfo(String PlateNumber,String ParkingID){
        Map<String,String> boostedGoalParkingMap=new HashMap<String,String>();
        List<String> boostedGoalParkingRequestList=new ArrayList<>();
        boostedGoalParkingRequestList.add("BusinessCode=GOS001");
        boostedGoalParkingRequestList.add("PlateNumber="+PlateNumber);
        boostedGoalParkingRequestList.add("ParkingID="+ParkingID);
        String sign=initParamsList(boostedGoalParkingRequestList);
        initParams(boostedGoalParkingMap);
        boostedGoalParkingMap.put("BusinessCode","GOS001");
        boostedGoalParkingMap.put("PlateNumber",PlateNumber);
        boostedGoalParkingMap.put("ParkingID",ParkingID);
        boostedGoalParkingMap.put("Sign",sign);
        Gson gson = new Gson();
        String json = "data="+gson.toJson(boostedGoalParkingMap);
        String url = configurationFiles.getBoostedGoalParkingUrl();
        ResponseEntity<String> responseEntity=barrierApiService.registeredTemp(json,url, Constants.APPLICATION_FORM);
        KeyTopPayValidateParkingLotInfo keyTopValidateParkingLotInfo=gson.fromJson(responseEntity.getBody(),KeyTopPayValidateParkingLotInfo.class);
        if(Constants.ADVANCE_RESERVATION_TIME_DEFAULT.equals(keyTopValidateParkingLotInfo.getStatus())){
            return  true;
        }
        return false;
    }

    /**
     * 支付临停费用
     * @param ParkingID
     * @param OrderID
     * @param PayWay
     * @param Amount
     * @param OnLineOrderID
     * @param PayTime
     * @return
     */
    public KeyTopParkingPayCostRespone payParkingLotCost(String ParkingID, String OrderID, String PayWay, String Amount, String OnLineOrderID, Long PayTime){
        Map<String,String> boostedGoalParkingMap=new HashMap<String,String>();
        List<String> boostedGoalParkingRequestList=new ArrayList<>();
        boostedGoalParkingRequestList.add("BusinessCode=PA0001");
        boostedGoalParkingRequestList.add("ParkingID="+ParkingID);
        boostedGoalParkingRequestList.add("OrderID="+OrderID);
        boostedGoalParkingRequestList.add("PayWay="+PayWay);
        boostedGoalParkingRequestList.add("Amount="+Amount);
        boostedGoalParkingRequestList.add("OnLineOrderID="+OnLineOrderID);
        boostedGoalParkingRequestList.add("PayTime="+DateUtils.timeConvertDateStrings(PayTime));
        String sign=initParamsList(boostedGoalParkingRequestList);
        initParams(boostedGoalParkingMap);
        boostedGoalParkingMap.put("BusinessCode","PA0001");
        boostedGoalParkingMap.put("ParkingID",ParkingID);
        boostedGoalParkingMap.put("OrderID",OrderID);
        boostedGoalParkingMap.put("PayWay",PayWay);
        boostedGoalParkingMap.put("Amount",Amount);
        boostedGoalParkingMap.put("OnLineOrderID",OnLineOrderID);
        boostedGoalParkingMap.put("PayTime",DateUtils.timeConvertDateStrings(PayTime));
        boostedGoalParkingMap.put("sign",sign);
        Gson gson = new Gson();
        String json = "data="+gson.toJson(boostedGoalParkingMap);
        String url = configurationFiles.getBoostedGoalParkingUrl();
        ResponseEntity<String> responseEntity=barrierApiService.registeredTemp(json,url, Constants.APPLICATION_FORM);
        KeyTopParkingPayCostRespone keyTopParkingPayCostRespone = new Gson().fromJson(responseEntity.getBody(), KeyTopParkingPayCostRespone.class);
        log.info("keyTopParkingPayCostRespone：{}",keyTopParkingPayCostRespone);
        return keyTopParkingPayCostRespone;
    }

    /**
     * 验证车牌是否存在白名单信息
     * @param ParkingID
     * @return
     */
    public boolean validateParkingLotNumberInfo(String PlateNumber,String ParkingID){
        Map<String,String> boostedGoalParkingMap=new HashMap<String,String>();
        List<String> boostedGoalParkingRequestList=new ArrayList<>();
        boostedGoalParkingRequestList.add("BusinessCode=WL0001");
        boostedGoalParkingRequestList.add("ParkingID="+ParkingID);
        String sign=initParamsList(boostedGoalParkingRequestList);
        initParams(boostedGoalParkingMap);
        boostedGoalParkingMap.put("BusinessCode","WL0001");
        boostedGoalParkingMap.put("ParkingID",ParkingID);
        boostedGoalParkingMap.put("Sign",sign);
        Gson gson = new Gson();
        String json = "data="+gson.toJson(boostedGoalParkingMap);
        String url = configurationFiles.getBoostedGoalParkingUrl();
        ResponseEntity<String> responseEntity=barrierApiService.registeredTemp(json,url, Constants.APPLICATION_FORM);
        KeyTopValidateParkingLotInfo keyTopValidateParkingLotInfo=gson.fromJson(responseEntity.getBody(),KeyTopValidateParkingLotInfo.class);
        if(Constants.BARRIER_MANUFACTURER_KEY_TOP.toString().equals(keyTopValidateParkingLotInfo.getStatus())){
            if(keyTopValidateParkingLotInfo.getData()!=null && keyTopValidateParkingLotInfo.getData().contains(PlateNumber)){
                return  true;
            }
        }
        return false;
    }
}
