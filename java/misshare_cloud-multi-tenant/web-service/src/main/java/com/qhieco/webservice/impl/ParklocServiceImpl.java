package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.iotdevice.Lock;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.LockRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.ParklocData;
import com.qhieco.response.data.web.ParklocEditData;
import com.qhieco.response.data.web.ParklocPublishData;
import com.qhieco.util.*;
import com.qhieco.webmapper.ParklocMapper;
import com.qhieco.webservice.ParklocService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-27 上午11:01
 * <p>
 * 类说明：
 * ${description}
 */
@Service
@Slf4j
public class ParklocServiceImpl implements ParklocService{

    @Autowired
    private ParklocRepository parklocRepository;

    @Autowired
    private LockRepository lockRepository;

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PublishRepository publishRepository;

    private QueryFunction<Parkloc,ParklocRequest> queryFunction;

    @Autowired
    private ParklocMapper parklocMapper;

    @Autowired
    private ParklotAmountRepository parklotAmountRepository;

    @Autowired
    private ParklotDistrictRepository parklotDistrictRepository;

    @PostConstruct
    public void init(){
        queryFunction = new QueryFunction<>(parklocRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private ParklocData transientProperty(Parkloc data){
        val newData = new ParklocData();
        BeanUtil.converJavaBean(data, newData);
        if(data.getParklotId()!= null){
            Parklot parklot=parklotRepository.findOne(data.getParklotId());
            newData.setParklot(parklot);
            if(parklot.getType()!=null){
                newData.setParklotTypeStr(Status.ParklotType.find(parklot.getType()));
            }
            if(data.getParklotDistrictId()!=null){
                newData.setParklotDistrictName(parklotDistrictRepository.findByIdAndState(data.getParklotDistrictId(),Status.Common.VALID.getInt()).getDistrictName());
            }
            newData.setParklotType(parklot.getType());
            newData.setInnershare(parklot.getInnershare());
            newData.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
        }
        if(data.getMobileUserId()!=null){
            newData.setUser(userMobileRepository.findOne(data.getMobileUserId()));
        }
        newData.setLock(lockRepository.findByParklocId(data.getId()));
        return newData;
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp query(ParklocRequest request){
        return queryFunction.query(request, where(request));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp addParkloc(ParklocAddRequest parklocAddRequest) {
        Integer parklotId = parklocAddRequest.getParklotId();
        Parklot parklot = parklotRepository.findOne(parklotId);
        if (Status.ParklotType.STATIC_PARK.getValue().equals(parklot.getType())) {
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOT_ID_BIND_PARKLOC);
        }
        Integer userId = parklocAddRequest.getUserId();
        if ((Status.ParklotType.YUE_CHE_CHANG.getValue().equals(parklot.getType()) && !parklot.getMobileUserId().equals(userId))) {
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOT_USER_BIND_PARKLOC);
        }
        Integer count=parklocRepository.findByParklocCount(parklocAddRequest.getParklotId());
        count+=1;
        Integer SignedAmount=0;
        ParklotAmount parklotAmount = parklotAmountRepository.findByParklotId(parklocAddRequest.getParklotId());
        if (parklotAmount != null) {
            SignedAmount=parklotAmount.getSignedAmount();
        }
        if(count>SignedAmount){
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOC_COUNT_ERROR);
        }
        String parklocNumber = parklocAddRequest.getNumber();
        Integer parklocId = parklocAddRequest.getParklocId();
        /*
         * 判断车位编号是否有重复
         */
        if (parklocNumberIsDuplicate(Arrays.asList(parklocNumber), parklotId, parklocId)) {
            log.error("车位编号重复，不能保存数据， parklocAddRequest = " + parklocAddRequest);
            throw new QhieWebException(Status.WebErr.DATA_DUPLICATE);
        }
        long now = System.currentTimeMillis();
        Parkloc parkloc;
        if (Constants.EMPTY_CAPACITY == parklocId) {
             parkloc = new Parkloc(parklocNumber, parklotId, userId, now, Status.Parkloc.UNPUBLISHED.getInt());
        } else {
            parkloc = parklocRepository.findOne(parklocId);
            parkloc.setNumber(parklocNumber);
            parkloc.setParklotId(parklotId);
            parkloc.setMobileUserId(userId);
            parkloc.setUpdateTime(now);
        }
        parkloc.setParklotDistrictId(parklocAddRequest.getParklotDistrictId());
        if (null == (parkloc = parklocRepository.save(parkloc))) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        Integer lockId = parklocAddRequest.getLockId();
        if (null != lockId && Constants.EMPTY_CAPACITY != lockId) {
            Lock lock = lockRepository.findOne(lockId);
            if (null == lock) {
                throw new QhieWebException(Status.WebErr.ILLEGAL_LOCK_ID);
            }

            // 解绑蓝牙，绑定新蓝牙
            List<Lock> locks = lockRepository.findByParklocId(parklocId);
            for (int i = 0; i < locks.size(); i++) {
                if(locks.get(i).getId().intValue()!=lock.getId().intValue()){
                    locks.get(i).setParklocId(null);
                }
            }

            lock.setParklocId(parkloc.getId());
            if (null == lockRepository.save(locks)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        }
        UserMobile user = userMobileRepository.findOne(userId);
        modifyUserType(user, parklot);
        return RespUtil.successResp();
    }

    /**
     * 变化身份
     * @param user 用户
     * @param parklot 停车区
     */
    private void modifyUserType(UserMobile user, Parklot parklot) {
        Integer parklotType = parklot.getType();
        if (Status.ParklotType.YUE_CHE_WEI.getValue().equals(parklotType)) {
            if (Constants.PARKING_RENTEE == user.getType()) {
                user.setType(Constants.PARKING_RENTER);
            }
        }
        if (null == userMobileRepository.save(user)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
    }

    @Override
    @Transactional
    public Resp batchAddParkloc(ParklocBatchAddRequest parklocBatchAddRequest) {
        List<String> numbers = parklocBatchAddRequest.getNumbers();
        Integer parklotId = parklocBatchAddRequest.getParklotId();
        Parklot parklot = parklotRepository.findOne(parklotId);
        if (Status.ParklotType.STATIC_PARK.getValue().equals(parklot.getType())) {
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOT_ID_BIND_PARKLOC);
        }
        Integer userId = parklocBatchAddRequest.getUserId();
        if ((Status.ParklotType.YUE_CHE_CHANG.getValue().equals(parklot.getType())  && !userId.equals(parklot.getMobileUserId()))) {
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOT_USER_BIND_PARKLOC);
        }
        Integer count=parklocRepository.findByParklocCount(parklocBatchAddRequest.getParklotId());
        Integer SignedAmount=0;
        ParklotAmount parklotAmount = parklotAmountRepository.findByParklotId(parklocBatchAddRequest.getParklotId());
        if (parklotAmount != null) {
            SignedAmount=parklotAmount.getSignedAmount();
        }
        count+=numbers.size();
        if(count>SignedAmount){
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOC_COUNT_ERROR);
        }
        /** *   *   判断车位编号是否有重复 start*   *   */
        /*
         * 判断车位编号是否有重复
         */
        if (parklocNumberIsDuplicate(numbers, parklotId, 0)) {
            log.error("车位编号重复，不能保存数据");
            throw new QhieWebException(Status.WebErr.DATA_DUPLICATE);
        }
        List<Parkloc> parklocs = new ArrayList<>();
        Parkloc parkloc;
        long now = System.currentTimeMillis();
        for (String number: numbers) {
            parkloc = new Parkloc(number, parklotId, userId, now,parklocBatchAddRequest.getParklotDistrictId(),Status.Parkloc.UNPUBLISHED.getInt());
            parklocs.add(parkloc);
        }
        batchInsert(parklocs);
        UserMobile user = userMobileRepository.findOne(userId);
        modifyUserType(user, parklot);
        return RespUtil.successResp();
    }

    @Transactional(rollbackFor = Exception.class)
    protected void batchInsert(List<Parkloc> parklocs) {
        for (int i = 0; i < parklocs.size(); i++) {
            entityManager.persist(parklocs.get(i));
            if (i % 10 == 0) {
                entityManager.flush();
                entityManager.clear();
            }
        }
    }

    @Override
    public Resp editParkloc(ParklocEditRequest parklocEditRequest) {
        Integer parklocId = parklocEditRequest.getParklocId();
        Parkloc parkloc = parklocRepository.findOne(parklocId);
        Integer parklotId = parkloc.getParklotId();
        String parklotName = parklotRepository.findOne(parklotId).getName();
        Integer userId = parkloc.getMobileUserId();
        String userName = userMobileRepository.findOne(userId).getName();
        String phone = userMobileRepository.findOne(userId).getPhone();
        String number = parkloc.getNumber();
        List<Lock> locks = lockRepository.findByParklocId(parklocId);
//        if (null != locks && locks.size() > Constants.ONE_CAPACITY) {
//            throw new QhieWebException(Status.WebErr.MORE_LOCK_BELONG_TO_ONE_PARKLOC);
//        }
        ParklocEditData parklocEditData = new ParklocEditData();
        parklocEditData.setParklotName(parklotName);
        parklocEditData.setParklotId(parklotId);
        parklocEditData.setUserName(null == userName? Constants.EMPTY_STRING: userName);
        parklocEditData.setUserId(userId);
        parklocEditData.setPhone(phone);
        parklocEditData.setNumber(number);
        parklocEditData.setParklotDistrictId(parkloc.getParklotDistrictId());
        if(parkloc.getParklotDistrictId()!=null){
            parkloc.setParklotDistrictName(parklotDistrictRepository.findByIdAndState(parkloc.getParklotDistrictId(),Status.Common.VALID.getInt()).getDistrictName());
        }
        parklocEditData.setMacName(null == locks || Constants.EMPTY_CAPACITY == locks.size()?
                Constants.EMPTY_STRING: locks.get(Constants.FIRST_INDEX).getBtName());
        parklocEditData.setLockId(null == locks || Constants.EMPTY_CAPACITY == locks.size()?
                Constants.EMPTY_CAPACITY: locks.get(Constants.FIRST_INDEX).getId());
        return RespUtil.successResp(parklocEditData);
    }

    @Override
    public Resp publishInfo(ParklocPublishInfoRequest parklocPublishInfoRequest) {
        Integer parklocId = parklocPublishInfoRequest.getParklocId();
        Parkloc parkloc = parklocRepository.findOne(parklocId);
        if (Status.Parkloc.UNPUBLISHED.getInt().equals(parkloc.getState())) {
            return RespUtil.successResp("当前该车位未发布");
        }
        Integer parklocState = parkloc.getState();
        List<Publish> publishList = publishRepository.findByParklocIdAndStateNot(parklocId, Status.Common.INVALID.getInt());
        List<ParklocPublishData> publishDataList = new ArrayList<>();
        ParklocPublishData parklocPublishData;
        for (Publish publish: publishList) {
            Integer state = publish.getState();
            String startTime = "";
            String endTime = "";
            Integer mode;
            String dayOfWeeks = "";
            if (Status.Publish.TOBEALTER.getInt().equals(state)) {
                mode = publish.getLastMode();
                dayOfWeeks = publish.getLastDayOfWeek();
                if(Constants.SINGLE_MODE.equals(mode)){
                    startTime = TimeUtil.timestamp2SpecialStr(publish.getLastStartTime());
                    endTime = TimeUtil.timestamp2SpecialStr(publish.getLastEndTime());
                }else{
                    startTime = TimeUtil.timestamp2SpecialStr2(publish.getLastStartTime());
                    endTime = TimeUtil.timestamp2SpecialStr2(publish.getLastEndTime());
                    Calendar calendar = Calendar.getInstance();
                    if(!TimeUtil.isSameDay(calendar,publish.getLastStartTime(),publish.getLastEndTime())){
                        endTime = "次日"+endTime;
                    }
                }

            } else {
                mode = publish.getMode();
                dayOfWeeks = publish.getDayOfWeek();
                if(Constants.SINGLE_MODE.equals(mode)){
                    startTime = TimeUtil.timestamp2SpecialStr(publish.getStartTime());
                    endTime = TimeUtil.timestamp2SpecialStr(publish.getEndTime());
                }else{
                    startTime = TimeUtil.timestamp2SpecialStr2(publish.getStartTime());
                    endTime = TimeUtil.timestamp2SpecialStr2(publish.getEndTime());
                    Calendar calendar = Calendar.getInstance();
                    if(!TimeUtil.isSameDay(calendar,publish.getStartTime(),publish.getEndTime())){
                        endTime = "次日"+endTime;
                    }
                }
            }
            parklocPublishData = new ParklocPublishData(startTime, endTime, mode, dayOfWeeks, parklocState);
            publishDataList.add(parklocPublishData);
        }
        return RespUtil.successResp(publishDataList);
    }

    private Specification<Parkloc> where(ParklocRequest request){
        return (root, query, cb) ->{
            val page = new PageableUtil(root,query,cb);
            page.like("number", request.getNumber());
            page.in("state",Arrays.stream(Status.Parkloc.values()).map(Status.Parkloc::getInt).collect(toList()));
            if (request.getParklotName() != null && !"".equals(request.getParklotName())) {
                val idList = parklocRepository.findByParklotName("%"+request.getParklotName()+"%");
                page.in("id", idList);
            }
            if (request.getLockSerialNumber() != null && !"".equals(request.getLockSerialNumber())){
                val idList = lockRepository.findParklocByNumber("%"+request.getLockSerialNumber()+"%");
                page.in("id", idList);
            }
            if (request.getUserName() != null && !"".equals(request.getUserName())){
                val idList = parklocRepository.findIdsByUserNameLike("%"+request.getUserName()+"%");
                page.in("id", idList);
            }
            if (request.getUserPhone() != null && !"".equals(request.getUserPhone())){
                val idList = parklocRepository.findIdsByPhoneLike("%"+request.getUserPhone()+"%");
                page.in("id", idList);
            }
            return page.pridect();
        };
    }

    @Override
    @EnableTenantFilter
    public Resp excel(ParklocRequest parklocRequest, OutputStream outputStream) throws IOException {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "id"));
        List<Parkloc> parklocList=parklocRepository.findAll(where(parklocRequest),sort);
        if (parklocList.size() == Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        List<ParklocData> list=new ArrayList<>();
        for(Parkloc parkloc:parklocList){
            list.add(transientProperty(parkloc));
        }
        ExcelUtil<ParklocData> excelUtil = new ExcelUtil<>(outputStream,ParklocData.class);
        excelUtil.write(list);
        return RespUtil.successResp();
    }

    /**
     * 判断车位编号是否有重复
     *
     * @return false 无重复
     */
    public boolean parklocNumberIsDuplicate(List<String> parklocNumbers, Integer parklotId, Integer parklocId) {
        if (parklocNumbers == null || parklocNumbers.size() == 0) {
            return false;
        }
        return parklocMapper.countByParklocNumberAdParklotId(parklocNumbers, parklotId, parklocId) > 0;
    }
}
