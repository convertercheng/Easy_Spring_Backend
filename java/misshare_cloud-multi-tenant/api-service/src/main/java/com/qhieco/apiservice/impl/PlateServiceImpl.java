package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ActivityService;
import com.qhieco.apiservice.PlateService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Plate;
import com.qhieco.commonentity.relational.UserPlateB;
import com.qhieco.commonrepo.PlateRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.commonrepo.UserPlateBRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.PlateMapper;
import com.qhieco.mapper.StatisticsMapper;
import com.qhieco.request.api.PlateAddRequest;
import com.qhieco.request.api.PlateDelRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ActivityRespData;
import com.qhieco.response.data.api.PlateRepData;
import com.qhieco.response.data.api.StatisticsData;
import com.qhieco.response.data.web.ActivityDetailData;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.QhieStringUtil;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/6 下午1:48
 * <p>
 * 类说明：
 *     车牌号Service实现类
 */
@Service
@Slf4j
public class PlateServiceImpl implements PlateService {

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private PlateMapper plateMapper;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private UserPlateBRepository userPlateBRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Override
    public List<PlateRepData> queryPlateListByUserId(Integer userId, Integer pageNum) {
        int startPage = pageNum * Constants.PAGE_SIZE;
        return plateMapper.queryPlateListByUserId(userId, Status.Common.VALID.getInt(), startPage, Constants.PAGE_SIZE);
    }

    @Override
    public Resp addPlate(PlateAddRequest plateAddRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(plateAddRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        //验证手机用户是否存在
        final Integer userId = plateAddRequest.getUser_id();
        if(null == userMobileRepository.findOne(userId)){
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        Long now = System.currentTimeMillis();
        //查询是否已经存在车牌号，如果不存在，保存车牌
        String plateNum = plateAddRequest.getPlate_num();
        plateNum = QhieStringUtil.removeSpace(plateNum);
        Plate resPlate = plateRepository.findByNumberAndState(plateNum,Status.Common.VALID.getInt());
        if(null == resPlate){
            Plate plate = new Plate(plateNum,now,Status.Common.VALID.getInt());
            resPlate = plateRepository.save(plate);
        }
        if(null != resPlate && null != resPlate.getId()){
            Integer resPlateId = resPlate.getId();
            //查询用户是否重复绑定同一车牌
            List<UserPlateB>  userPlateBs = userPlateBRepository.findByMobileUserIdAndPlateIdAndState(userId, resPlateId, Status.Common.VALID.getInt());
            if(null != userPlateBs && userPlateBs.size() > Constants.EMPTY_CAPACITY){
                throw new QhieException(Status.ApiErr.REPEAT_ADD_PLATE);
            }
            UserPlateB userPlateB = new UserPlateB(userId, resPlateId, Status.Common.VALID.getInt(),now);
            UserPlateB resUserPlateB = userPlateBRepository.save(userPlateB);
            if(null == resUserPlateB){
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
            /*
             * 绑定车牌活动统计数据
             */
            saveStaticeData(resUserPlateB);
        }else{
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return RespUtil.successResp();
    }

    /**
     * 绑定车牌活动统计 表:t_statistics
     * @param
     */
    protected void saveStaticeData(UserPlateB userPlateB) {
        List<ActivityRespData> activityList = activityService.findActivityByType(Status.ActivityType.REGISTE.getInt());
        if (activityList == null || activityList.size()==0) {
            return;
        }
        Integer activityId = 0;
        StatisticsData.StatisticsBean stati= null;
        for (ActivityRespData item: activityList) {
            StatisticsData.StatisticsBean data = new StatisticsData.StatisticsBean();
            data.setType(2);
            data.setUserId(userPlateB.getMobileUserId());
            data.setActivityId(item.getId());
            data.setValue(1);
            data.setCreateTime(System.currentTimeMillis());
            //参与统计
            statisticsMapper.saveStatisticsData(data);

            //触发统计
            data.setType(4);
            statisticsMapper.saveStatisticsData(data);
            if(item.getType().intValue()==Status.ActivityType.BINDING_PLATE.getInt()){
                activityId = item.getId().intValue();
                stati = new StatisticsData.StatisticsBean();
                stati=data;
            }
        }
        if (activityId == 0 || stati == null) {
            return;
        }

        // 2 绑车牌
        // 1 注册
        // 3 支付
        // 获取活动阶梯类型
        // 如果不存在阶梯类型就retuen，终止后面的操作。
        List<ActivityRespData> itemList = activityService.findActivityTriggerTypeById(activityId);
        Integer userId = userPlateB.getMobileUserId();
        for(int i = 1; i<itemList.size();i++){
            Integer type = itemList.get(i).getTriggerType();
            if(type.intValue()==1){
                // 2
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.REGISTE.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }else if(type.intValue()==2){
                // 4
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.BINDING_PLATE.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }else if(type.intValue()==3){
                // 3
                Integer sums = statisticsMapper.findActivityByTriggerType(userId, Status.ActivityType.FIRST_ORDER.getInt());
                if(sums==null||sums==0){
                    return;
                }
            }
        }

        //获奖统计
        stati.setType(3);
        statisticsMapper.saveStatisticsData(stati);
    }

    @Transactional
    @Override
    public Resp delPlate(PlateDelRequest plateDelRequest) {
        String timestamp = plateDelRequest.getTimestamp();
        if (StringUtils.isEmpty(timestamp) || CommonUtil.isTimeStampInValid(timestamp)) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        String[] plateIds = plateDelRequest.getPlate_id().split(Constants.DELIMITER_COMMA);
        int userId = plateDelRequest.getUser_id();
        for (String plateId: plateIds) {
            List<UserPlateB> userPlateBs = userPlateBRepository.findByMobileUserIdAndPlateIdAndState(userId, Integer.parseInt(plateId), Status.Common.VALID.getInt());
            if (null == userPlateBs || Constants.EMPTY_CAPACITY == userPlateBs.size()) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_USER_PLATE);
            }
            UserPlateB userPlateB = userPlateBs.get(Constants.FIRST_INDEX);
            userPlateB.setState(Status.Common.DELETED.getInt());
            if (null == userPlateBRepository.save(userPlateB)) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
        }
        return RespUtil.successResp();
    }
}
