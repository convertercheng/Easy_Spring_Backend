package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.ActivityService;
import com.qhieco.apiservice.UserService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.relational.UserRegisterB;
import com.qhieco.commonrepo.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.BankCardMapper;
import com.qhieco.mapper.CouponMapper;
import com.qhieco.mapper.StatisticsMapper;
import com.qhieco.mapper.UserMapper;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.api.UserLoginRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.*;
import com.qhieco.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/2/26 下午9:27
 * <p>
 * 类说明：
 * UserService的实现类
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private SmsRepository smsRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CouponMapper couponMapper;

    @Autowired
    private BankCardMapper bankCardMapper;

    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private RegisterRepository registerRepository;

    @Autowired
    private UserRegisterBRepository userRegisterBRepository;

    @Autowired
    private LogOperationMobileRepository logOperationMobileRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private ActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserLoginRepData login(UserLoginRequest userLoginRequest) {
        UserLoginRepData userLoginRepData = new UserLoginRepData();
        String phone = userLoginRequest.getPhone();
        String jpushId = userLoginRequest.getJpush_id();
        String ip = userLoginRequest.getIp();
        if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
            throw new QhieException(Status.ApiErr.PHONE_NUM_ERROR);
        }
        if (CommonUtil.isTimeStampInValid(userLoginRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (CommonUtil.isJpushIdInValid(jpushId)) {
            throw new QhieException(Status.ApiErr.ERROR_JPUSH_ID);
        }
        UserMobile user = userMobileRepository.findByPhone(phone);
        UserLoginRepData.UserBean userBean;
        if (user == null) {
            log.info("初始化一个新用户");
            userBean = registerNewUser(phone, jpushId);
        } else {
            log.info("用户已存在，查询用户的相关信息");
            userBean = loginUser(user, jpushId);
            if("H5".equals(jpushId) || "wechat app".equals(jpushId)){
                if(userBean.getType()==Status.userType.USERTYPE_TWO.getValue()){
                    throw new QhieException(Status.ApiErr.ADMIN_NAME_TYPE);
                }
            }
        }
        /**
         * 保存用户登录日志信息
         */
        saveLogOperationMobile(userLoginRequest, ip, userBean);
        if (StringUtils.isNotEmpty(userLoginRequest.getUnionId())) {
            UserExtraInfo userExtraInfos = userExtraInfoRepository.findByUserExtraInfo(userLoginRequest.getUnionId());
            if (userExtraInfos != null) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_WX_OPENID);
            }
            userExtraInfos = userExtraInfoRepository.findByUserExtraInfoId(userBean.getId());
            if (userExtraInfos != null && userExtraInfos.getWxUnionId() != null) {
                throw new QhieException(Status.ApiErr.NONEXISTENT_WX_USER);
            }
            if (userExtraInfos == null) {
                userExtraInfos = new UserExtraInfo();
            }
            userExtraInfos.setMobileUserId(userBean.getId());
            userExtraInfos.setWxUnionId(userLoginRequest.getUnionId());
            userExtraInfos.setWxBindOpenId(userLoginRequest.getOpenId());
            userExtraInfoRepository.save(userExtraInfos);

        }
        if (Status.userType.USERTYPE_TWO.getValue() == userBean.getType()) {
            Parklot parklot = parklotRepository.findByMobileUserIdAndState(userBean.getId(), Status.Common.VALID.getInt());
            if (parklot != null && Status.ParklotType.YUE_CHE_WEI.getValue().equals(parklot.getType())) {
                userBean.setType(Constants.PARKING__APPOINTMENT_INNER);
            }
            if (parklot != null && Status.ParklotType.YUE_CHE_CHANG.getValue().equals(parklot.getType())) {
                userBean.setType(Constants.PARKING_SHARE_INNER);
            }
        }
        removeExcessState(phone);
        userLoginRepData.setUser(userBean);
        UserRegisterB userRegisterB = new UserRegisterB();
        userRegisterB.setIdentification(userLoginRequest.getIdentification());
        saveUpdateUserRegisterB(userRegisterB);
        return userLoginRepData;
    }

    /**
     * 保存用户登录日志信息
     *
     * @param userLoginRequest
     * @param ip
     * @param userBean
     */
    private void saveLogOperationMobile(UserLoginRequest userLoginRequest, String ip, UserLoginRepData.UserBean userBean) {
        LogOperationMobile logOperationMobile = new LogOperationMobile();
        logOperationMobile.setMobileUserId(userBean.getId());
        logOperationMobile.setOperateTime(System.currentTimeMillis());
        logOperationMobile.setSourceIp(ip);
        logOperationMobile.setType(Status.LogOperateType.TYPE_LOGIN.getInt());
        logOperationMobile.setSourceModel(userLoginRequest.getPhoneModel());
        logOperationMobile.setContent("用户登录");
        logOperationMobileRepository.save(logOperationMobile);
    }

    /**
     * 绑定用户注册来源标记
     *
     * @param userRegisterB
     */
    private void saveUpdateUserRegisterB(UserRegisterB userRegisterB) {
        UserRegisterB userRegisterB1 = userRegisterBRepository.findUserRegisterBByIdentification(userRegisterB.getIdentification(), Status.Common.VALID.getInt());
        if (userRegisterB1 != null) {
            if (userRegisterB1.getMoblieUserId() == null) {
                userRegisterB1.setMoblieUserId(userRegisterB.getMoblieUserId());
                userRegisterB1.setUpdateTime(System.currentTimeMillis());
                userRegisterBRepository.save(userRegisterB1);
            }
            eidt(userRegisterB1.getId());
        }
    }

    /**
     * 修改用户UV量
     *
     * @param id
     */
    private void eidt(Integer id) {
        Register register = registerRepository.findOne(id);
        register.setUv(register.getUv() + 1);
        registerRepository.save(register);
    }

    /**
     * 清除用户刷验证码的状态
     *
     * @param phone 手机号码
     */
    private void removeExcessState(String phone) {
        List<SMS> smsList = smsRepository.findByPhoneOrderByIdDesc(phone);
        if (null != smsList && smsList.size() > Constants.EMPTY_CAPACITY) {
            SMS sms = smsList.get(Constants.FIRST_INDEX);
            sms.setState(Status.Common.VALID.getInt());
            if (null == smsRepository.save(sms)) {
                throw new QhieException(Status.ApiErr.INSERT_ERROR);
            }
        }
    }

    /**
     * 注册一个新用户：
     *
     * @param phone   手机号码
     * @param jpushId 极光推送id
     * @return 返回userBean
     */
    @Transactional(rollbackFor = SQLException.class)
    protected UserLoginRepData.UserBean registerNewUser(String phone, String jpushId) {
        UserLoginRepData.UserBean userBean = new UserLoginRepData.UserBean();
        /*
         * 初始化user表
         */
        UserMobile userMobile = saveUserMobile(phone);
        /*
         * 初始化三方信息表
         */
        saveUserExtraInfo(userMobile, jpushId);
        /*
         * 初始化用户金额表
         */
        saveBalanceUser(userMobile);

        /*
         * 注册活动统计数据
         */
        saveStaticeData(userMobile);

        /*
         * 设置UserBean的属性
         */
        userBean.setId(userMobile.getId());
        userBean.setType(userMobile.getType());
        userBean.setToken(Constants.TOKEN);
        return userBean;
    }

    /**
     * 将数据存储到表t_user_mobile中
     *
     * @param phone 手机号码
     */
    protected UserMobile saveUserMobile(String phone) {
        UserMobile userMobile = new UserMobile();
        userMobile.setPhone(phone);
        userMobile.setIsIndexOrder(0);
        userMobile.setRegisterTime(System.currentTimeMillis());
        userMobile.setLatestLoginTime(System.currentTimeMillis());
        userMobile.setType(Constants.PARKING_RENTEE);
        userMobile.setState(Status.Common.VALID.getInt());
        userMobile.setIntegral(Constants.USER_LEVEL);
        return userMobileRepository.save(userMobile);
    }

    /**
     * 存储数据到三方表t_user_extra_info中
     *
     * @param userMobile 存储的userMobile
     */
    protected void saveUserExtraInfo(UserMobile userMobile, String jpushId) {
        UserExtraInfo userExtraInfo = new UserExtraInfo();
        userExtraInfo.setMobileUserId(userMobile.getId());
        userExtraInfo.setJpushRegId(jpushId);
        userExtraInfoRepository.save(userExtraInfo);
        //清空表中极光ID一样的数据
        Long start = System.currentTimeMillis();
        CompletableFuture<Void> updateFuture = CompletableFuture.runAsync(() -> {
            updateSameJpushRegId(userMobile.getId(), jpushId);
        }, executor).exceptionally(e -> {
            log.error("faild to update jpushID:" + e.getCause());
            e.printStackTrace();
            return null;
        });
        log.info("update jpush time:{}", System.currentTimeMillis() - start);
    }

    static ExecutorService executor = Executors.newFixedThreadPool(10, new ThreadFactory() {
        int count = 1;

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "custom-executor-" + count++);
        }
    });

    synchronized private void updateSameJpushRegId(Integer id, String jpushId) {
        userExtraInfoRepository.updateSameJpushRegId(id, jpushId);
    }

    /**
     * 存储数据到用户金额表中t_balance_user
     *
     * @param userMobile 存储的userMobile
     */
    protected void saveBalanceUser(UserMobile userMobile) {
        BalanceUser balanceUser = new BalanceUser();
        balanceUser.setMobileUserId(userMobile.getId());
        balanceUser.setState(Status.Common.VALID.getInt());
        balanceUserRepository.save(balanceUser);
    }

    /**
     * 注册活动统计 表:t_statistics
     * @param
     */
    protected void saveStaticeData(UserMobile userMobile) {
        if(userMobile == null){
            return;
        }else{
            if(userMobile.getIsIndexOrder()==1){
                return;
            }
        }
        List<ActivityRespData> activityList = activityService.findActivityByType(Status.ActivityType.INVITE.getInt());
        if (activityList == null || activityList.size()==0) {
            return;
        }
        Integer activityId = 0;
        StatisticsData.StatisticsBean stati= null;
        for (ActivityRespData item: activityList) {
            StatisticsData.StatisticsBean data = new StatisticsData.StatisticsBean();
            data.setType(2);
            data.setUserId(userMobile.getId());
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
        Integer userId = userMobile.getId();
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

    /**
     * 登录查询用户的信息
     *
     * @param user    用户
     * @param jpushId 极光推送id
     * @return 返回userBean
     */
    protected UserLoginRepData.UserBean loginUser(UserMobile user, String jpushId) {
        UserLoginRepData.UserBean userBean = new UserLoginRepData.UserBean();
        Integer userId = user.getId();
        //更新三方信息表&&单点登录
        updateUserExtraInfo(userId, jpushId);
        updateLastestLoginTime(user);
        userBean.setId(userId);
        userBean.setType(user.getType());
        userBean.setToken(Constants.TOKEN);
        BalanceUser balanceUser=balanceUserRepository.findByMobileUserIdAndState(userId,Status.Common.VALID.getInt());
        if(balanceUser==null){
            saveBalanceUser(user);
        }
        return userBean;
    }

    /**
     * 更新最新的登录时间
     *
     * @param user UserMobile
     */
    protected void updateLastestLoginTime(UserMobile user) {
        user.setLatestLoginTime(System.currentTimeMillis());
        if (null == userMobileRepository.save(user)) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
    }

    /**
     * 更新三方信息表
     *
     * @param userId  用户id
     * @param jpushId 极光推送id
     */
    protected void updateUserExtraInfo(int userId, String jpushId) {
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(userId);
        if (userExtraInfo != null) {
            String oldJpushId = userExtraInfo.getJpushRegId();
            if (oldJpushId!=null &&  !"".equals(oldJpushId) && !jpushId.equals(oldJpushId)) {
                //如果本次传入极光Id和之前存的不一样，推送消息
                QhPushUtil.getInstance().sendQhPush(oldJpushId, QhMessageType.CUSTOM,
                        QhMessageTemplate.SIGIN_OUT, "");
                log.info("send {}: sigin out message, oldJpushId:{}", userId, oldJpushId);
            }
            //清空表中极光ID一样的数据
            userExtraInfoRepository.updateSameJpushRegId(userId, jpushId);
            userExtraInfo.setJpushRegId(jpushId);
            userExtraInfoRepository.save(userExtraInfo);
        }
    }

    @Override
    public PersonCenterRepData queryPersonCenterInfo(Integer userId) {
        // 查询用户可使用的卡券数量
        int couponCount = couponMapper.queryCountUserCouponByUserId(userId, Status.Coupon.COUPON_CONVERTIBILITY.getInt());
        // 查询用户类型、头像路径和预约时段
        String avatarPath = userMapper.queryAvatarPathByUserId(userId);
        HashMap<String, Object> userInfo = userMapper.queryUserInfoByUserId(userId);
        String reservationPeriod = userMapper.queryReservationPeriodByUserId(userId);
        PersonCenterRepData personCenterRepData = new PersonCenterRepData();
        personCenterRepData.setCouponCount(couponCount);
        if (userInfo.get("type") != null) {
            Integer userType = Integer.parseInt(userInfo.get("type").toString());
            if (Status.userType.USERTYPE_TWO.getValue().equals(userType)) {
                Parklot parklot = parklotRepository.findByMobileUserIdAndState(userId, Status.Common.VALID.getInt());
                if (parklot != null && Status.ParklotType.YUE_CHE_WEI.getValue().equals(parklot.getType())) {
                    personCenterRepData.setUserType(Constants.PARKING__APPOINTMENT_INNER);
                }
                if (parklot != null && Status.ParklotType.YUE_CHE_CHANG.getValue().equals(parklot.getType())) {
                    personCenterRepData.setUserType(Constants.PARKING_SHARE_INNER);
                }
            }
            if (personCenterRepData.getUserType() == null) {
                personCenterRepData.setUserType(Integer.parseInt(userInfo.get("type").toString()));
            }
        }
        personCenterRepData.setPhone(userInfo.get("phone").toString());
        personCenterRepData.setAvatarPath(StringUtils.isEmpty(avatarPath) ? avatarPath : configurationFiles.getPicPath() + avatarPath);
        personCenterRepData.setReservePeriod(reservationPeriod);
        return personCenterRepData;
    }

    @Override
    public UserWithdrawAmountRepData queryUserWithdrawAmountByUserId(Integer userId) {
        Integer userType = userMapper.queryUserTypeByUserId(userId);
        BigDecimal withdrawAmount = null;
        if (userType == Constants.PARKING_ADMIN) {
            withdrawAmount = userMapper.queryParklotAmountByUserId(userId);
        } else {
            withdrawAmount = userMapper.queryUserWithdrawAmountByUserId(userId);
        }
        UserWithdrawAmountRepData userWithdrawAmountRepData = bankCardMapper.queryBankCardInfoByUserId(userId, Status.Common.VALID.getInt());
        if (userWithdrawAmountRepData == null) {
            userWithdrawAmountRepData = new UserWithdrawAmountRepData();
        }
        userWithdrawAmountRepData.setWithdrawAmount(withdrawAmount);
        return userWithdrawAmountRepData;
    }

    @Transactional
    @Override
    public Resp uploadAvatar(MultipartFile file, Integer userId, String timestamp) {
        if (StringUtils.isEmpty(timestamp) || CommonUtil.isTimeStampInValid(timestamp)) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        if (file.isEmpty()) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_AVATAR_FILE);
        }
        UserMobile userMobile;
        if (null == (userMobile = userMobileRepository.findOne(userId))) {
            throw new QhieException(Status.ApiErr.NONEXISTENT_USER);
        }
        FileNormalizeUtil.FileNormalize fileNormalize = FileNormalizeUtil.getFileNormalize(file, FileNormalizeUtil.FileType.AVATAR);
        String filePath = fileNormalize.getPath();
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(configurationFiles.getWebUploadPath() + filePath);
            Files.write(path, bytes);
        } catch (IOException e) {
            return RespUtil.errorResp(Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getCode(), Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getMsg());
        }
        saveFileInfo(userMobile, fileNormalize);
        return RespUtil.successResp(filePath);
    }

    /**
     * 保存文件信息
     *
     * @param userMobile 移动用户
     * @param fileNormalize   标准化文件插入数据库格式
     */
    @Transactional(rollbackFor = Exception.class)
    protected void saveFileInfo(UserMobile userMobile, FileNormalizeUtil.FileNormalize fileNormalize) {
        /*
         * 存储信息进入File表
         */
        String name = fileNormalize.getName();
        String path = fileNormalize.getPath();
        String intro = fileNormalize.getIntro();
        File avatarFile = new File(name, path, intro, System.currentTimeMillis(), Status.Common.VALID.getInt());
        if (null == (avatarFile = fileRepository.save(avatarFile))) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        /*
         * 用户表与file表关联
         */
        Integer fileId = avatarFile.getId();
        userMobile.setFileId(fileId);
        userMobileRepository.save(userMobile);
    }

    @Override
    public String findByUserExtraInfo(Map<String, String> wxMap, String state) {
        String wxUnionId = wxMap.get("unionId");
        String openId = wxMap.get("openId");
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByUserExtraInfo(wxUnionId);
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("?openId=" + openId + "&unionId=" + wxUnionId);
        String url = configurationFiles.getMpUrlPrefix() + "ihomeh5/#/login";
        if (userExtraInfo != null && userExtraInfo.getMobileUserId() != null) {
            url = configurationFiles.getMpUrlPrefix() + "ihomeh5";
            UserMobile userMobile = userMobileRepository.findOne(userExtraInfo.getMobileUserId());
            stringBuffer.append("&id=" + userMobile.getId());
            if (userMobile != null && userMobile.getType() != null) {
                stringBuffer.append("&type=" + userMobile.getType());
                if (userMobile.getType() == Status.userType.USERTYPE_TWO.getValue().byteValue()) {
                    url = configurationFiles.getMpUrlPrefix() + "ihomeh5/#/login";
                }
            }
            return url + stringBuffer.toString();
        }
        return url + stringBuffer.toString();
    }
}
