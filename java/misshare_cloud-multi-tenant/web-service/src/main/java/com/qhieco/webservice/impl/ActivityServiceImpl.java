package com.qhieco.webservice.impl;

import com.qhieco.commonentity.*;
import com.qhieco.commonentity.relational.ActivityTagB;
import com.qhieco.commonentity.relational.LadderPrizeB;
import com.qhieco.commonrepo.*;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ActivityQuery;
import com.qhieco.request.web.ActivitySameTimeQuery;
import com.qhieco.request.web.PrizeEntityRequest;
import com.qhieco.request.web.PrizeReceiveListRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.*;
import com.qhieco.webmapper.ActivityMapper;
import com.qhieco.webmapper.FileMapper;
import com.qhieco.webmapper.PrizeMapper;
import com.qhieco.webmapper.PrizeReceiveRecordMapper;
import com.qhieco.webservice.ActivityService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 19:50
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityRuleRepository activityRuleRepository;

    @Autowired
    private LadderRepository ladderRepository;

    @Autowired
    private LadderPrizeBRepository ladderPrizeBRepository;

    @Autowired
    private ActivityRuleTriggerRepository activityRuleTriggerRepository;

    @Autowired
    private ActivityTagBRepository activityTagBRepository;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private PrizeRepositoty prizeRepositoty;

    @Autowired
    private PrizeMapper prizeMapper;

    @Autowired
    private PrizeReceiveRecordMapper prizeReceiveRecordMapper;

    @Override
    public void deleteActivity(Integer id) {
        activityMapper.updateActivityState(id, Status.Common.INVALID.getInt());
    }

    @Override
    public ActivityDetailData queryActivityDetailInfo(Integer id) {
        ActivityDetailData data = activityMapper.queryActivityDetailInfoById(id);
        if (data != null) {
            if (null != data.getFileLongPath()) {
                data.setFileLongPath(configurationFiles.getPicPath() + data.getFileLongPath());
            }
            if (null != data.getFileWidePath()) {
                data.setFileWidePath(configurationFiles.getPicPath() + data.getFileWidePath());
            }
        }
        return data;
    }

    @Override
    public AbstractPaged<ActivityInfoData> queryActivityList(ActivityQuery request) {
        if (StringUtils.isEmpty(request.getState())) {
            request.setState(-1);
        }
        int count = activityMapper.queryCountActivityList(request);
        List<ActivityInfoData> activityListDataList = null;
        if (count > 0) {
            activityListDataList = activityMapper.queryActivityList(request);
        }
        AbstractPaged<ActivityInfoData> data = AbstractPaged.<ActivityInfoData>builder()
                .sEcho(request.getSEcho() + 1)
                .dataList(activityListDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp saveOrUpdate(Activity activity, MultipartFile fileLong, MultipartFile fileWide, Integer fileLongId, Integer fileWideId, List<ActivityRule> activityRules, List<ActivityTagB> activityTagBs, Long now) {
        ActivitySameTimeQuery queryParam = new ActivitySameTimeQuery();
        queryParam.setBeginTime(activity.getBeginTime());
        queryParam.setEndTime(activity.getEndTime());
        queryParam.setType(activity.getType());
        queryParam.setId(activity.getId());
        //判断活动时间是否有相交
        int count = activityMapper.findSameTimeActivity(queryParam);
        if (count > 0) {
            throw new QhieWebException(Status.WebErr.DATA_DUPLICATE);
        }
        if (null != activity.getId()) {
            Activity activityDB = activityRepository.findOne(activity.getId());
            if (null == activityDB) {
                throw new QhieWebException(Status.WebErr.ILLEGAL_ALL);
            }
            //判断活动是否已经开始，或者已经停止，如果是不能修改
            if (!activityDB.getState().equals(Status.Common.VALID.getInt()) || activityDB.getBeginTime() <= now) {
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }
            activity.setCreateTime(activityDB.getCreateTime());
            //删除所有关联的规则
            activityRuleRepository.updateStateByActivityId(Status.Common.INVALID.getInt(), activity.getId());
            //删除所有关联的标签
            activityTagBRepository.updateStateByActivityId(Status.Common.INVALID.getInt(), activity.getId());
        }
        //  保存活动信息
        activity = activityRepository.save(activity);
        if (null == activity) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        try {
            // 处理新上传图片
            // 长图
            if (fileLong != null) {
                Integer oldFileLongId = fileLongId;
                fileLongId = saveFile(fileLong, fileLongId);
                saveOrUpdateActivityFileB(oldFileLongId, fileLongId, activity.getId(), Status.ActivityFileState.INDEX.getInt());
            }else{
                if(null != fileLongId){
                    fileMapper.updateActivityFileBByFileId(fileLongId);
                }
            }
            // 宽图
            if (fileWide != null) {
                Integer oldFileWideId = fileWideId;
                fileWideId = saveFile(fileWide, fileWideId);
                saveOrUpdateActivityFileB(oldFileWideId, fileWideId, activity.getId(), Status.ActivityFileState.LIST.getInt());

            }else{
                if(null != fileWideId){
                    fileMapper.updateActivityFileBByFileId(fileWideId);
                }
            }
        } catch (Exception e) {
            throw new QhieWebException(Status.WebErr.SYSTEM_ERROR);
        }
        //循环添加规则
        if (null != activityRules) {
            for (ActivityRule activityRule : activityRules) {
                if (null == activityRule.getType()) {
                    log.error("规则类型为空");
                    throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                }
                ActivityRule temp = new ActivityRule(activity.getId(), activityRule.getType(), now, Status.Common.VALID.getInt());
                ActivityRule activityRuleResp = activityRuleRepository.save(temp);
                if (null == activityRuleResp) {
                    throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                }
                //循环添加触发条件
                List<Trigger> triggers = activityRule.getTriggers();
                if (null != triggers) {
                    for (Trigger trigger : triggers) {
                        ActivityRuleTrigger activityRuleTrigger = new ActivityRuleTrigger(activityRuleResp.getId(), trigger.getTriggerType(), now, Status.Common.VALID.getInt());
                        if (null == activityRuleTriggerRepository.save(activityRuleTrigger)) {
                            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                        }
                    }
                } else {
                    log.error("规则下的触发条件为空");
                    throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                }
                //循环添加阶梯
                List<Ladder> ladders = activityRule.getLadders();
                if (null != ladders) {
                    for (Ladder ladder : ladders) {
                        if (null == ladder.getType()) {
                            log.error("阶梯类型为空");
                            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                        }
                        if (ladder.getType() == Constants.LADDER_TYPE_INVITER) {
                            if (null == ladder.getLadderStep() || null == ladder.getInviteNumber() || ladder.getInviteNumber() == 0) {
                                log.error("阶梯号或者邀请数量为空");
                                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                            }
                            if (activity.getType() != Status.ActivityType.INVITE.getInt()) {
                                log.error("活动类型不为邀请时，不能存在邀请人类型的阶梯");
                                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                            }
                        } else {
                            if (null != ladder.getLadderStep() || null != ladder.getInviteNumber()) {
                                log.error("阶梯号或者邀请数量不为空");
                                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                            }
                        }
                        ladder.setActivityRuleId(activityRuleResp.getId());
                        ladder.setCreateTime(now);
                        ladder.setState(Status.Common.VALID.getInt());
                        Ladder ladderResp = ladderRepository.save(ladder);
                        if (null == ladderResp) {
                            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                        }
                        //循环添加奖品
                        List<LadderPrizeB> prizes = ladder.getPrizes();
                        if (null != prizes) {
                            for (LadderPrizeB ladderPrizeB : prizes) {
                                if (null == ladderPrizeB.getPrizeId() || null == ladderPrizeB.getPrizeNumber() || ladderPrizeB.getPrizeNumber() == 0) {
                                    log.error("奖品ID或者数量为空");
                                }
                                ladderPrizeB.setLadderId(ladderResp.getId());
                                ladderPrizeB.setCreateTime(now);
                                ladderPrizeB.setState(Status.Common.VALID.getInt());
                                if (null == ladderPrizeBRepository.save(ladderPrizeB)) {
                                    throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                                }
                            }
                        } else {
                            log.error("阶梯下的奖品为空");
                            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                        }
                    }
                } else {
                    log.error("规则下的阶梯为空");
                    throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                }
            }
        }
        //循环添加标签
        if (null != activityTagBs) {
            for (ActivityTagB activityTagB : activityTagBs) {
                if (null == activityTagB.getTagId() || null == activityTagB.getType()) {
                    log.error("标签tagId或者Type为空");
                    throw new QhieWebException(Status.WebErr.PARAM_ERROR);
                }
                activityTagB.setActivityId(activity.getId());
                activityTagB.setCreateTime(now);
                activityTagB.setState(Status.Common.VALID.getInt());
                if (null == activityTagBRepository.save(activityTagB)) {
                    throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                }
            }
        }
        return RespUtil.successResp();
    }

    public void saveOrUpdateActivityFileB(Integer oldFileId, Integer newFileId, Integer activityId, Integer state) {
        // 保存或者更新关联表数据，如果oldFileId不为空，则已上传图片，更新即可
        if (oldFileId != null) {
            fileMapper.updateActivityFileB(oldFileId, newFileId);
        } else {
            fileMapper.insertActivityFileB(activityId, newFileId, state);
        }
    }

    /**
     * 保存文件
     *
     * @param multipartFile
     * @param fileId
     * @return
     * @throws Exception
     */
    public Integer saveFile(MultipartFile multipartFile, Integer fileId) throws Exception {
        String originalName = multipartFile.getOriginalFilename();
        FileNormalizeUtil.FileNormalize fileNormalize = upload(multipartFile);
        String name = fileNormalize.getName();
        String path = fileNormalize.getPath();
        String intro = fileNormalize.getIntro();
        // 删除原文件
        if (fileId != null) {
            String oldFilePath = fileMapper.queryPathById(fileId);
            FileUploadUtils.clear(configurationFiles.getWebUploadPath() + oldFilePath);
            fileMapper.deleteById(fileId);
        }
        // 保存文件数据
        File file = new File(name, path, intro, System.currentTimeMillis(), Status.Common.VALID.getInt());
        file = fileRepository.save(file);
        fileId = file.getId();
        return fileId;
    }

    /**
     * 上传文件并删除原来的文件
     *
     * @param multipartFile
     * @throws Exception
     */
    public FileNormalizeUtil.FileNormalize upload(MultipartFile multipartFile) {
        FileNormalizeUtil.FileNormalize fileNormalize = FileNormalizeUtil.getFileNormalize(multipartFile, FileNormalizeUtil.FileType.ACTIVITY);
        String filePath = fileNormalize.getPath();
        try {
            byte[] bytes = multipartFile.getBytes();
            Path path = Paths.get(configurationFiles.getWebUploadPath() + filePath);
            Files.write(path, bytes);
        } catch (IOException e) {
            log.error("upload activity file error {}", e);
        }
        return fileNormalize;
    }

    @Override
    public Prize saveOrUpdatePrize(PrizeEntityRequest request) throws Exception {
        // 处理上传图片
        if (request.getFiles() != null && request.getFiles().size() > 0) {
            request.setFileId(saveFile(request.getFiles().get(0), request.getFileId()));
        }
        Prize prize;
        if (StringUtils.isEmpty(request.getId())) {
            prize = new Prize();
            prize.setCreateTime(System.currentTimeMillis());
            prize.setState(Status.Common.VALID.getInt());
        } else {
            prize = prizeRepositoty.findOne(request.getId());
        }
        prize.setName(request.getName());
        prize.setType(request.getType());
        prize.setMoney(request.getMoney());
        prize.setIntro(request.getIntro());
        prize.setFileId(request.getFileId());
        prize.setStartTime(request.getStartTime());
        prize.setEndTime(request.getEndTime());
        prize.setModifyTime(System.currentTimeMillis());
        prize = prizeRepositoty.save(prize);
        return prize;
    }

    @Override
    public int countRepeatPrizeName(Integer id, String name) {
        return prizeMapper.countRepeatPrizeName(id, name);
    }

    @Override
    public PrizeEntityDetailData queryPrizeDetailById(Integer id) {
        PrizeEntityDetailData prizeEntityDetailData = prizeMapper.queryPrizeById(id);
        if (prizeEntityDetailData != null) {
            prizeEntityDetailData.setFilePath(configurationFiles.getPicPath() + prizeEntityDetailData.getFilePath());
        }
        return prizeEntityDetailData;
    }

    @Override
    public AbstractPaged<PrizeEntityDetailData> queryPrizeList(String name, Integer startPage, Integer pageSize, Integer sEcho) {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("name", name);
        params.put("startPage", startPage);
        params.put("pageSize", pageSize);
        int count = prizeMapper.queryCountPrizeListByCondition(params);
        List<PrizeEntityDetailData> prizeEntityDetailDataList = null;
        if (count > 0) {
            prizeEntityDetailDataList = prizeMapper.queryPrizeListByCondition(params);
            for (PrizeEntityDetailData prizeEntityDetailData : prizeEntityDetailDataList) {
                prizeEntityDetailData.setFilePath(configurationFiles.getPicPath() + prizeEntityDetailData.getFilePath());
            }
        }
        AbstractPaged<PrizeEntityDetailData> data = AbstractPaged.<PrizeEntityDetailData>builder()
                .sEcho(sEcho + 1)
                .dataList(prizeEntityDetailDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public AbstractPaged<PrizeReceiveRecordData> queryPrizeReceiveList(PrizeReceiveListRequest request) {
        HashMap<String, Object> params = new HashMap<>(16);
        if (request.getStart() != null && request.getLength() != null) {
            params.put("startPage", request.getStart());
            params.put("pageSize", request.getLength());
        }
        params.put("prizeName", request.getPrizeName());
        params.put("phone", request.getPhone());
        params.put("triggerType", request.getTriggerType());
        if (request.getTime() != null) {
            params.put("time", TimeUtil.timeStamp2Date(request.getTime(), "yyyyMMdd"));
        }
        int count = prizeReceiveRecordMapper.queryCountPrizeReceiveListByCondition(params);
        List<PrizeReceiveRecordData> prizeReceiveRecordDataList = null;
        if (count > 0) {
            prizeReceiveRecordDataList = prizeReceiveRecordMapper.queryPrizeReceiveListByCondition(params);
        }
        AbstractPaged<PrizeReceiveRecordData> data = AbstractPaged.<PrizeReceiveRecordData>builder()
                .sEcho(request.getSEcho() == null ? 1 : request.getSEcho() + 1)
                .dataList(prizeReceiveRecordDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public Resp queryPrizeReceiveListExcel(PrizeReceiveListRequest request, OutputStream outputStream) throws Exception {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("prizeName", request.getPrizeName());
        params.put("phone", request.getPhone());
        params.put("triggerType", request.getTriggerType());
        if (request.getTime() != null) {
            params.put("time", TimeUtil.timeStamp2Date(request.getTime(), "yyyyMMdd"));
        }
        params.put("startPage", 0);
        params.put("pageSize", Constants.EXCEL_SIZE);
        int count = prizeReceiveRecordMapper.queryCountPrizeReceiveListByCondition(params);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }

        List<PrizeReceiveRecordData>  prizeReceiveRecordDataList = prizeReceiveRecordMapper.queryPrizeReceiveListByCondition(params);

        for (PrizeReceiveRecordData prizeReceiveRecordData : prizeReceiveRecordDataList) {
            String str = "";
            if (!StringUtils.isEmpty(prizeReceiveRecordData.getTriggerTypes())) {
                String[] arr = prizeReceiveRecordData.getTriggerTypes().split(Constants.DELIMITER_COMMA);
                for (String s : arr) {
                    try {
                        str += Status.TriggerType.find(Integer.valueOf(s)) + ";";
                    } catch (Exception e) {
                        log.error("" + e);
                    }
                }
            }
            prizeReceiveRecordData.setTriggerTypeStr(str);
        }
        ExcelUtil<PrizeReceiveRecordData> excelUtil = new ExcelUtil<>(outputStream, PrizeReceiveRecordData.class);
        excelUtil.write(prizeReceiveRecordDataList);
        return RespUtil.successResp();
    }

    @Override
    public List<PrizeIdAdNameInfoData> queryValidPrizeAll(Long startTime, Long endTime) {
        return prizeMapper.queryValidPrizeAll(startTime, endTime);
    }

    /**
     * 判断是否可以删除奖品，true，表示可以删除
     *
     * @param prizeId
     * @return
     */
    @Override
    public Resp checkDeletePrizeCondition(Integer prizeId) {

        // 奖品绑定的活动是否有效(活动未过期)
        boolean isValidActivityPrize = activityMapper.countValidActivityPrizeByPrizeId(prizeId) > 0;
        if (isValidActivityPrize) {
            return RespUtil.errorResp(Status.WebErr.PRIZE_DEL_ERROR_ACTIVITY_VALID.getCode(), Status.WebErr.PRIZE_DEL_ERROR_ACTIVITY_VALID.getMsg());
        }

        // 查询此奖品是否有用户领取了但是尚未过期
        boolean isValidReceivePrize = prizeMapper.countValidReceivePrize(prizeId) > 0;
        if (isValidReceivePrize) {
            return RespUtil.errorResp(Status.WebErr.PRIZE_DEL_ERROR_USER_VALID.getCode(), Status.WebErr.PRIZE_DEL_ERROR_USER_VALID.getMsg());
        }

        return RespUtil.successResp();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp deletePrize(Integer id) {
        Resp resp = checkDeletePrizeCondition(id);
        if (resp.getError_code().intValue() == Status.WebErr.SUCCESS.getCode()) {
            prizeMapper.updatePrizeStateById(id, Status.Common.DELETED.getInt());
        }
        return resp;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp frozenPrize(Integer id) {
        Resp resp = checkFrozenPrize(id);
        if (resp.getError_code().intValue() == Status.WebErr.SUCCESS.getCode()) {
            prizeMapper.updatePrizeStateById(id, Status.PrizeState.FROZEN.getInt());
        }
        return resp;
    }

    @Override
    public Resp checkFrozenPrize(Integer id) {
        boolean isValidPrize = prizeMapper.countValidPrizeById(id) > 0;
        if (!isValidPrize) {
            return RespUtil.errorResp(Status.WebErr.PRIZE_FROZEN_ERR_TIMEOUT.getCode(), Status.WebErr.PRIZE_FROZEN_ERR_TIMEOUT.getMsg());
        }
        return RespUtil.successResp();
    }

    @Override
    public Resp unfreezePrize(Integer id) {
        Resp resp = checkUnfreezePrize(id);
        if (resp.getError_code().intValue() == Status.WebErr.SUCCESS.getCode()) {
            prizeMapper.updatePrizeStateById(id, Status.Common.VALID.getInt());
        }
        return resp;
    }

    @Override
    public Resp checkUnfreezePrize(Integer id) {
        Integer state = prizeMapper.queryUnexpiredPrizeState(id);
        if (state == null) {
            return RespUtil.errorResp(Status.WebErr.PRIZE_UNFREEZE_ERR_TIMEOUT.getCode(), Status.WebErr.PRIZE_UNFREEZE_ERR_TIMEOUT.getMsg());
        }
        if (!state.equals(Status.PrizeState.FROZEN.getInt())) {
            return RespUtil.errorResp(Status.WebErr.PRIZE_UNFREEZE_ERR_STATE.getCode(), Status.WebErr.PRIZE_UNFREEZE_ERR_STATE.getMsg());
        }
        return RespUtil.successResp();
    }

    /**
     * 查询"邀请"活动参与用户列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Activity> queryActivityByInviteList(ActivityQuery request) {
        return null;
    }

    /**
     * 查询"注册"活动参与用户列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Activity> queryActivityByRegisterList(ActivityQuery request) {
        return null;
    }

    /**
     * 查询"绑定车牌"活动参与详情列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Activity> queryActivityByBindCarPlateList(ActivityQuery request) {
        return null;
    }

    /**
     * 查询"首次"下单活动参与详情列表
     *
     * @param request
     * @return
     */
    @Override
    public List<Activity> queryActivityByFirstOrerList(ActivityQuery request) {
        return null;
    }
}
