package com.qhieco.apiservice.impl;


import com.qhieco.apiservice.DiscountPackageService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.OrderParking;
import com.qhieco.commonentity.PackageOrder;
import com.qhieco.commonentity.Plate;
import com.qhieco.commonrepo.OrderParkingRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.DiscountPackageMapper;
import com.qhieco.mapper.PlateMapper;
import com.qhieco.mapper.UserMapper;
import com.qhieco.request.api.DiscountPackageOrderRequest;
import com.qhieco.request.api.DiscountPackageRequest;
import com.qhieco.request.api.UserPackageRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.DiscountPackageData;
import com.qhieco.response.data.api.DiscountPackageListData;
import com.qhieco.response.data.api.DiscountRuleTimeData;
import com.qhieco.response.data.api.UserPackageData;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/7/18 13:57
 * <p>
 * 类说明：
 * 停车场优惠套餐service实现
 */
@Service
@Slf4j
public class DiscountPackageServiceImpl implements DiscountPackageService {

    @Autowired
    DiscountPackageMapper discountPackageMapper;

    @Autowired
    OrderParkingRepository orderParkingRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PlateMapper plateMapper;

    @Override
    public Resp getDiscountPackageInfo(DiscountPackageRequest request) {

        DiscountPackageData discountPackageData = discountPackageMapper.getDiscountPackageInfo(request.getParklotId());
        if (discountPackageData != null) {
            discountPackageData.setParklotId(request.getParklotId());
            List<DiscountRuleTimeData> discountRuleTimeDataList = discountPackageMapper.findRuleTimeList(discountPackageData.getId());
            DiscountRuleTimeData discountRuleTimeData1 = new DiscountRuleTimeData();
            DiscountRuleTimeData discountRuleTimeData2 = new DiscountRuleTimeData();
            DiscountRuleTimeData discountRuleTimeData3 = new DiscountRuleTimeData();
            for (DiscountRuleTimeData d : discountRuleTimeDataList) {
                if (Constants.TIME_TYPE_ONE.equals(d.getState())) {
                    if (discountRuleTimeData1.getState() != null) {
                        discountRuleTimeData1.setTimeStr(discountRuleTimeData1.getTimeStr() + ";" + d.getBeginTime() + "-" + d.getEndTime());
                    }
                    if (discountRuleTimeData1.getState() == null) {
                        discountRuleTimeData1.setState(d.getState());
                        discountRuleTimeData1.setType(d.getType());
                        discountRuleTimeData1.setTimeStr(d.getBeginTime() + "-" + d.getEndTime());

                    }
                }
                if (Constants.TIME_TYPE_TWO.equals(d.getState())) {
                    if (discountRuleTimeData2.getState() != null) {
                        discountRuleTimeData2.setTimeStr(discountRuleTimeData2.getTimeStr() + ";" + d.getBeginTime() + "-" + d.getEndTime());
                    }

                    if (discountRuleTimeData2.getState() == null) {
                        discountRuleTimeData1.setType(d.getType());
                        discountRuleTimeData2.setState(d.getState());
                        discountRuleTimeData2.setTimeStr(d.getBeginTime() + "-" + d.getEndTime());
                    }
                }
                if (Constants.TIME_TYPE_THREE.equals(d.getState())) {

                    if (discountRuleTimeData3.getState() != null) {
                        discountRuleTimeData3.setTimeStr(discountRuleTimeData3.getTimeStr() + ";" + d.getBeginTime() + "-" + d.getEndTime());
                    }

                    if (discountRuleTimeData3.getState() == null) {
                        discountRuleTimeData1.setType(d.getType());
                        discountRuleTimeData3.setState(d.getState());
                        discountRuleTimeData3.setTimeStr(d.getBeginTime() + "-" + d.getEndTime());
                    }
                }
            }
            discountRuleTimeDataList = new ArrayList<>();
            if (discountRuleTimeData1.getState() != null) {
                discountRuleTimeDataList.add(discountRuleTimeData1);
            }
            if (discountRuleTimeData2.getState() != null) {
                discountRuleTimeDataList.add(discountRuleTimeData2);
            }
            if (discountRuleTimeData3.getState() != null) {
                discountRuleTimeDataList.add(discountRuleTimeData3);
            }
            discountPackageData.setRuleTimeList(discountRuleTimeDataList);
            discountPackageData.setPackageFormatSumDataList(discountPackageMapper.
                    findPackageFormatSum(discountPackageData.getId()));
            List<String> list=discountPackageMapper.findPackageByParklotName(discountPackageData.getId());
            String parklotNames="";
            for(String parklotName:list){
                parklotNames+=parklotName+";";
            }
            if(!StringUtils.isEmpty(parklotNames)){
                parklotNames=parklotNames.substring(0,parklotNames.length()-1);
            }
            discountPackageData.setParkLotNameList(parklotNames);

            if (request.getMobileUserId() != null) {
                setPlateInfo(request.getMobileUserId(), discountPackageData);
            }
        }

        return RespUtil.successResp(discountPackageData);
    }


    @Override
    public Resp savePackageOrder(DiscountPackageOrderRequest request) throws Exception {
        UserPackageRequest userPackageRequest=new UserPackageRequest();
        userPackageRequest.setPackageId(request.getPackageId());
        userPackageRequest.setParklotId(request.getParklotId());
        userPackageRequest.setPlateId(request.getPlateId());
        userPackageRequest.setState(3);
        UserPackageData userPackageData=discountPackageMapper.findUserPackage(userPackageRequest);
        if(userPackageData==null){
            Integer count=discountPackageMapper.findUserPackageByCount(userPackageRequest);
            log.info("停车场ID="+request.getParklotId()+"车牌ID="+request.getPlateId()+"request.getPackageId()"
            +"数量："+count);
            DiscountPackageData discountPackageData= discountPackageMapper.getDiscountPackageInfo(request.getParklotId());
            if(count>=discountPackageData.getToplimit()){
                throw new QhieException(Status.ApiErr.PACKAGE_ERROR);
            }
        }
        PackageOrder packageOrder=new PackageOrder();
        packageOrder.setCreateTime(System.currentTimeMillis());
        packageOrder.setDiscountFee(new BigDecimal(0));
        packageOrder.setPackageId(request.getPackageId());
        return null;
    }

    /**
     * 设置车牌信息
     *
     * @param userId              用户id
     * @param discountPackageData 返回数据
     */
    private void setPlateInfo(Integer userId, DiscountPackageData discountPackageData) {
        OrderParking orderParking = orderParkingRepository.findTopByMobileUserIdOrderByIdDesc(userId);
        Integer plateId;
        String plateNo;
        if (null == orderParking) {
            // 没有下过单，则查询最后添加的车牌号
            Plate plate = userMapper.queryPlateByUserId(userId);
            plateId = null == plate ? Constants.EMPTY_CAPACITY : plate.getId();
            plateNo = null == plate ? Constants.EMPTY_STRING : plate.getNumber();
        } else {
            // 下过单，查询最后一次下单的车牌号
            HashMap plateMap = plateMapper.queryPlateInfoByPlateIdAdUserId(orderParking.getPlateId(), orderParking.getMobileUserId(),
                    Status.Common.VALID.getInt());
            if (plateMap != null && plateMap.containsKey("plateNo") && plateMap.containsKey("plateId")) {
                plateId = Integer.valueOf(plateMap.get("plateId").toString());
                plateNo = plateMap.get("plateNo").toString();
            } else {
                // 如果最后一次下单的车牌号不可用，查询最后添加的车牌号
                Plate plate = userMapper.queryPlateByUserId(userId);
                plateId = null == plate ? Constants.EMPTY_CAPACITY : plate.getId();
                plateNo = null == plate ? Constants.EMPTY_STRING : plate.getNumber();
            }
        }
        discountPackageData.setPlateId(plateId);
        discountPackageData.setPlateNo(plateNo);
    }

    @Override
    public List<DiscountPackageListData> queryPackageList(Integer userId) {

        // 查询用户所有的可用车牌
        List<Integer> plateIds = plateMapper.queryPlateIdsByUserId(userId);
        if (plateIds.size() == 0) {
            return new ArrayList<>();
        }
        // 查询套餐基本信息
        List<DiscountPackageListData> packageListDataList = discountPackageMapper.queryPackageListByPlateId(plateIds);

        for (DiscountPackageListData packageListData : packageListDataList) {
            // 遍历查询每个套餐的小区和可停时间

            // 套餐关联车场
            List<DiscountPackageListData.ParklotInfo> parklotInfos =
                    discountPackageMapper.queryPackageParklotInfoByPackageId(packageListData.getPackageId());
            // 可停时间
            List<DiscountPackageListData.TimeRule> timeRules =
                    discountPackageMapper.queryPackageTimeRuleByPackageId(packageListData.getPackageId());

            packageListData.setParklotInfos(parklotInfos);
            packageListData.setTimeRules(timeRules);
        }

        return packageListDataList;
    }
}
