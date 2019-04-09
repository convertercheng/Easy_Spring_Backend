package com.qhieco.webservice.impl;

import com.qhieco.anotation.AddTenantInfo;
import com.qhieco.anotation.EnableTenantFilter;
import com.qhieco.commonentity.*;
import com.qhieco.commonentity.relational.ParklotFeeRuleParkingB;
import com.qhieco.commonentity.relational.ParklotFeeRuleReserveB;
import com.qhieco.commonentity.relational.ParklotFileB;
import com.qhieco.commonentity.relational.ParklotParamsB;
import com.qhieco.commonrepo.*;
import com.qhieco.commonrepo.iot.AccessRepository;
import com.qhieco.commonrepo.iot.BalanceParklotRepository;
import com.qhieco.commonrepo.iot.BarrierRepository;
import com.qhieco.commonrepo.iot.RelaymeterRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotFileRemoveRequest;
import com.qhieco.request.web.ParklotInfoRequest;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.Md5Util;
import com.qhieco.util.PageableUtil;
import com.qhieco.util.PhoneFormatCheckUtils;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.DiscountPackageMapper;
import com.qhieco.webmapper.FeeRuleMapper;
import com.qhieco.webmapper.ParklotMapper;
import com.qhieco.webmapper.ParklotParamsMapper;
import com.qhieco.webservice.ParklotService;
import com.qhieco.webservice.exception.QhieWebException;
import com.qhieco.webservice.impl.wx.WxService;
import com.qhieco.webservice.utils.QueryFunction;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.qhieco.commonentity.Parklot.PARKLOT_TYPE_LIMIT;
import static com.qhieco.constant.Constants.*;
import static java.util.stream.Collectors.toList;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/26 下午2:22
 * <p>
 * 类说明：
 * Parklot业务层实现类
 */
@Slf4j
@Service
public class ParklotServiceImpl implements ParklotService {

    @Autowired
    private ParklotRepository parklotRepository;

    @Autowired
    private ParklotAmountRepository parklotAmountRepository;

    @Autowired
    private ParklotFileBRepository parklotFileBRepository;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AreaServiceImpl areaService;

    @Autowired
    private AreaRepository areaRepository;

    private QueryFunction<Parklot, ParklotRequest> queryFunction;

    @Autowired
    private ParklotParamsBRepository parklotParamsBRepository;

    @Autowired
    private ParklotFeeRuleReserveBRepository parklotFeeRuleReserveBRepository;

    @Autowired
    private ParklotFeeRuleParkingBRepository parklotFeeRuleParkingBRepository;

    @Autowired
    private ParklotMapper parklotMapper;

    @Autowired
    private ParklotParamsMapper parklotParamsMapper;

    @Autowired
    private BarrierRepository barrierRepository;

    @Autowired
    private AccessRepository accessRepository;

    @Autowired
    private RelaymeterRepository relaymeterRepository;

    @Autowired
    private UserMobileRepository userMobileRepository;

    @Autowired
    private BalanceUserRepository balanceUserRepository;

    @Autowired
    private BalanceParklotRepository balanceParklotRepository;

    @Autowired
    private OrderParkingRepository orderParkingRepository;

    @Autowired
    private OrderWithdrawRepository orderWithdrawRepository;

    @Autowired
    private OrderRefundRepository orderRefundRepository;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private FeeRuleMapper feeRuleMapper;

    @Autowired
    private WxService wxService;

    @Autowired
    private DiscountPackageMapper discountPackageMapper;

    @PostConstruct
    public void init() {
        queryFunction = new QueryFunction<>(parklotRepository);
        queryFunction.setTransientProperty(this::transientProperty);
    }

    private Parklot transientProperty(Parklot data) {
        List<Area> areaList = areaService.findAreaList(data.getAreaId());
        StringBuilder address = new StringBuilder();
        areaList.forEach(area -> address.append(area.getName()));
        ParklotAmount amount = parklotAmountRepository.findByParklotId(data.getId());
        if (amount != null) {
            data.setAddress(address.toString() + data.getAddress());
            data.setTotalAmount(amount.getTotalAmount());
            data.setSignedAmount(amount.getSignedAmount());
        }
        for (int k = 0; k < areaList.size(); k++) {
            switch (k) {
                case 0:
                    data.setProvinceName(areaList.get(k).getName());
                    break;
                case 1:
                    data.setCityName(areaList.get(k).getName());
                    break;
                case 2:
                    data.setAreaName(areaList.get(k).getName());
                    break;
                default:
                    log.info("找不到对应的信息");
                    break;
            }
        }
        return data;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @EnableTenantFilter
    public Resp addNewParklot(ParklotInfoRequest parklotInfoRequest) {
        log.info("请求 {}", parklotInfoRequest);
        Integer parklotId = parklotInfoRequest.getParklotId();
        log.info(Constants.EMPTY_CAPACITY == parklotId ? "新增小区" : "编辑小区");
        boolean isEdit = Constants.EMPTY_CAPACITY != parklotId;
        log.info("经纬度 {}", parklotInfoRequest.getLatLng());
        log.info("入口经纬度 {}", parklotInfoRequest.getNaviLatLng());
        boolean isDuplicate = isDuplicateLngLat(parklotInfoRequest.getLatLng(), parklotId);
        if (isDuplicate) {
            log.error("新增或者编辑小区，传入的经纬度与其他小区有重复，保存失败。parklotId {}", parklotId);
            return RespUtil.errorResp(Status.WebErr.PARKLOT_LNGLAT_DUPLICATE.getCode(), Status.WebErr.PARKLOT_LNGLAT_DUPLICATE.getMsg());
        }
        Parklot parklot = parklotSave(parklotInfoRequest, parklotId, isEdit);
        parklotId = parklot.getId();
        initParklotBalance(isEdit, parklotId);
        Integer type = parklot.getType();
        ParklotAmount parklotAmount = isEdit ? parklotAmountRepository.findByParklotId(parklotId) : new ParklotAmount();
        Integer totalAmount = parklotInfoRequest.getTotalAmount();
        Integer signedAmount = parklotInfoRequest.getSignedAmount();
        parklotAmount.setTotalAmount(totalAmount);
        parklotAmount.setSignedAmount(signedAmount);
        if (!isEdit) {
            log.info("初始化车位数量：publishAmount, idleAmount, reserveAmount, reservableAmount, leftAmount, leftAmountType");
            parklotAmount.setPublishAmount(Constants.EMPTY_CAPACITY);
            parklotAmount.setIdleAmount(Constants.EMPTY_CAPACITY);
            parklotAmount.setReservedAmount(Constants.EMPTY_CAPACITY);
            parklotAmount.setReservableAmount(Constants.EMPTY_CAPACITY);
            parklotAmount.setLeftAmount(Constants.EMPTY_CAPACITY);
            parklotAmount.setLeftAmountType(Constants.EMPTY_CAPACITY);
            parklotAmount.setRelayUpdateTime(System.currentTimeMillis());
        }
        if (Constants.PARKING_STATIC != type) {
            log.info("非车位查询类型");
            saveAdmin(parklot, parklotInfoRequest, isEdit);
            parklotFeeRuleBind(parklotInfoRequest, parklotId, isEdit);
            parklotTimeSetting(parklotInfoRequest, parklotId, isEdit);
            parklotAlloc(parklotInfoRequest, parklotId, isEdit);
        } else {
            log.info("车位查询类型");
            Integer leftAmount = parklotInfoRequest.getLeftAmount();
            Integer isShow = parklotInfoRequest.getIsShow();
            if (leftAmount == null || isShow == null) {
                log.error("缺少空车位数量或是否显示参数");
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }
            parklotAmount.setLeftAmount(leftAmount);
            parklotAmount.setLeftAmountType(isShow);
        }
        parklotAmount.setModifyTime(System.currentTimeMillis());
        parklotAmount.setParklotId(parklotId);
        if (null == parklotAmountRepository.save(parklotAmount)) {
            log.error("车位数量存储错误");
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        Integer packageId = parklotInfoRequest.getPackageId();
        if(packageId!=null) {
            Integer packageState = parklotInfoRequest.getPackageState();
            discountPackageMapper.delParklotByPackage(parklotId, packageId);
            discountPackageMapper.saveParklotByPackage(parklotId, packageId, packageState);
        }

        log.info("存储文件");
        List<MultipartFile> files = parklotInfoRequest.getFiles();
        List<Integer> fileIds = parklotInfoRequest.getFileIds();
        log.info("fileIds is {}", fileIds);
        log.info("files is {}", files);
        if (saveParklotFiles(parklotId, files, fileIds)) {
            return RespUtil.errorResp(Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getCode(), Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getMsg());
        }
        return RespUtil.successResp();
    }

    /**
     * 初始化车场费用
     *
     * @param isEdit    是否编辑
     * @param parklotId 车场id
     */
    @Transactional(rollbackFor = Exception.class)
    protected void initParklotBalance(boolean isEdit, Integer parklotId) {
        if (!isEdit) {
            log.info("初始化车场费用");
            BalanceParklot balanceParklot = new BalanceParklot();
            balanceParklot.setParklotId(parklotId);
            balanceParklot.setBalance(Constants.BIGDECIMAL_ZERO);
            if (null == balanceParklotRepository.save(balanceParklot)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        }
    }

    /**
     * 判断经纬度是否有重复
     *
     * @param latLng    经纬度
     * @param parklotId 停车区id
     * @return 是否重复
     */
    private boolean isDuplicateLngLat(String latLng, Integer parklotId) {
        Double lng = Double.valueOf(latLng.split(Constants.DELIMITER_COMMA)[0]);
        Double lat = Double.valueOf(latLng.split(Constants.DELIMITER_COMMA)[1]);
        return parklotMapper.queryParklotLnglatDuplicateByCondition(parklotId, lng, lat) > 0;
    }

    @Transactional(rollbackFor = Exception.class)
    protected boolean saveParklotFiles(Integer parklotId, List<MultipartFile> files, List<Integer> fileIds) {
        if (null != fileIds && Constants.EMPTY_CAPACITY != fileIds.size()) {
            for (int i = 0; i < fileIds.size(); i++) {
                Integer fileId = fileIds.get(i);
                MultipartFile file = files.get(i);
                ParklotFileB parklotFileB;
                if (Constants.EMPTY_CAPACITY == fileId && null != file) {
                    log.info("add new file");
                    String fileName = Md5Util.getFileName(file.getOriginalFilename());
                    String filePath = configurationFiles.getWebUploadPath() + Constants.DIRECTORY_PARKLOT + fileName;
                    log.info("file path is {}", filePath);
                    if (writeFileToDisk(file, filePath)) {
                        return true;
                    }
                    filePath = Constants.DIRECTORY_PARKLOT + fileName;
                    File parklotFile = saveFile(fileName, filePath);
                    parklotFileB
                            = new ParklotFileB(parklotId, parklotFile.getId(), Status.Common.VALID.getInt(), System.currentTimeMillis(), System.currentTimeMillis());
                    if (null == parklotFileBRepository.save(parklotFileB)) {
                        throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                    }
                } else if (Constants.EMPTY_CAPACITY != fileId && null != file) {
                    log.info("replace file");
                    String fileName = Md5Util.getFileName(file.getOriginalFilename());
                    String filePath = configurationFiles.getWebUploadPath() + Constants.DIRECTORY_PARKLOT + fileName;
                    log.info("file path is {}", filePath);
                    if (writeFileToDisk(file, filePath)) {
                        return true;
                    }
                    filePath = Constants.DIRECTORY_PARKLOT + fileName;
                    File parklotFile = saveFile(fileName, filePath);
                    parklotFileB = parklotFileBRepository.findByParklotIdAndFileIdAndState(parklotId, fileId, Status.Common.VALID.getInt());
                    parklotFileB.setFileId(parklotFile.getId());
                    if (null == parklotFileBRepository.save(parklotFileB)) {
                        throw new QhieWebException(Status.WebErr.INSERT_ERROR);
                    }
                }
            }
        }
        return false;
    }

    private boolean writeFileToDisk(MultipartFile file, String filePath) {
        log.info("write file into disk");
        try {
            byte[] fileBytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return true;
        }
        log.info("finish write file");
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    protected File saveFile(String fileName, String filePath) {
        File parklotFile = new File();
        parklotFile.setName(fileName);
        parklotFile.setPath(filePath);
        parklotFile.setIntro(Constants.DIRECTORY_PARKLOT);
        parklotFile.setCreateTime(System.currentTimeMillis());
        parklotFile.setState(Status.Common.VALID.getInt());
        if (null == (parklotFile = fileRepository.save(parklotFile))) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        return parklotFile;
    }

    @Transactional(rollbackFor = Exception.class)
    protected void parklotAlloc(ParklotInfoRequest parklotInfoRequest, Integer parklotId, boolean isEdit) {
        Integer platformPercent = parklotInfoRequest.getPlatformPercent();
        Integer ownerPercent = parklotInfoRequest.getOwnerPercent();
        Integer propCompPercent = parklotInfoRequest.getPropCompPercent();
        Integer operatorPercent = parklotInfoRequest.getOperatorPercent();
        Integer platformAppointmentPercent = parklotInfoRequest.getPlatformAppointmentPercent();
        Integer propCompAppointmentPercent = parklotInfoRequest.getPropCompAppointmentPercent();
        final long now = System.currentTimeMillis();
        ParklotParamsB platformParklotParamB = null;
        if (isEdit) {
            platformParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, PLATFORM_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (platformParklotParamB == null) {
            platformParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), EMPTY_STRING, ALLOCATION_CODE);
            platformParklotParamB.setQhKey(PLATFORM_PERCENTAGE);
            platformParklotParamB.setSequence(0);
        }
        platformParklotParamB.setQhValue(null == platformPercent ? PLATFORM_PERCENTAGE_DEFAULT : String.valueOf(platformPercent));
        if (null == parklotParamsBRepository.save(platformParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB ownerParklotParamB = null;
        if (isEdit) {
            ownerParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, OWNER_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (ownerParklotParamB == null) {
            ownerParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), EMPTY_STRING, ALLOCATION_CODE);
            ownerParklotParamB.setQhKey(OWNER_PERCENTAGE);
            ownerParklotParamB.setSequence(1);
        }
        ownerParklotParamB.setQhValue(null == ownerPercent ? OWNER_PERCENTAGE_DEFAULT : String.valueOf(ownerPercent));
        if (null == parklotParamsBRepository.save(ownerParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB propcompParklotParamB = null;
        if (isEdit) {
            propcompParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, PROPCOMP_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (propcompParklotParamB == null) {
            propcompParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.ALLOCATION_CODE);
            propcompParklotParamB.setQhKey(PROPCOMP_PERCENTAGE);
            propcompParklotParamB.setSequence(2);
        }
        propcompParklotParamB.setQhValue(null == propCompPercent ? PROPCOMP_PERCENTAGE_DEFAULT : String.valueOf(propCompPercent));
        if (null == parklotParamsBRepository.save(propcompParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB operatorParklotParams = null;
        if (isEdit) {
            operatorParklotParams = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, OPERATOR_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (operatorParklotParams == null) {
            operatorParklotParams = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), EMPTY_STRING, ALLOCATION_CODE);
            operatorParklotParams.setQhKey(OPERATOR_PERCENTAGE);
            operatorParklotParams.setSequence(3);
        }
        operatorParklotParams.setQhValue(null == operatorPercent ? OPERATOR_PERCENTAGE_DEFAULT : String.valueOf(operatorPercent));
        if (null == parklotParamsBRepository.save(operatorParklotParams)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }

        ParklotParamsB platformAppointmentParklotParams = null;
        if (isEdit) {
            platformAppointmentParklotParams = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, PLATFORM_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (platformAppointmentParklotParams == null) {
            platformAppointmentParklotParams = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), EMPTY_STRING, ALLOCATION_CODE);
            platformAppointmentParklotParams.setQhKey(PLATFORM_APPOINTMENT_PERCENTAGE);
            platformAppointmentParklotParams.setSequence(4);
        }
        platformAppointmentParklotParams.setQhValue(null == platformAppointmentPercent ? PLATFORM_APPOINTMENT_PERCENTAGE_DEFAULT : String.valueOf(platformAppointmentPercent));
        if (null == parklotParamsBRepository.save(platformAppointmentParklotParams)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB propCompAppointmentParklotParams = null;
        if (isEdit) {
            propCompAppointmentParklotParams = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, PROPCOMP_APPOINTMENT_PERCENTAGE, Status.Common.VALID.getInt());
        }
        if (propCompAppointmentParklotParams == null) {
            propCompAppointmentParklotParams = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), EMPTY_STRING, ALLOCATION_CODE);
            propCompAppointmentParklotParams.setQhKey(PROPCOMP_APPOINTMENT_PERCENTAGE);
            propCompAppointmentParklotParams.setSequence(5);
        }
        propCompAppointmentParklotParams.setQhValue(null == propCompAppointmentPercent ? PROPCOMP_APPOINTMENT_PERCENTAGE_DEFAULT : String.valueOf(propCompAppointmentPercent));
        if (null == parklotParamsBRepository.save(propCompAppointmentParklotParams)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void parklotTimeSetting(ParklotInfoRequest parklotInfoRequest, Integer parklotId, boolean isEdit) {
        Integer minPublishInterval = parklotInfoRequest.getMinPublishInterval();
        Integer minSharePeriod = parklotInfoRequest.getMinSharePeriod();
        Integer freeCancellationTime = parklotInfoRequest.getFreeCancellationTime();
        Integer maxDelayTime = parklotInfoRequest.getMaxDelayTime();
        Integer minChargePeriod = parklotInfoRequest.getMinChargePeriod();
        Integer advanceChangeTime = parklotInfoRequest.getAdvanceChangeTime();
        Integer advanceReservationTime = parklotInfoRequest.getAdvanceReservationTime();
        final long now = System.currentTimeMillis();
        ParklotParamsB minPublishParklotParamB = null;
        if (isEdit) {
            minPublishParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, MIN_PUBLISH_INTERVAL, Status.Common.VALID.getInt());
        }
        if (minPublishParklotParamB == null) {
            minPublishParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            minPublishParklotParamB.setQhKey(MIN_PUBLISH_INTERVAL);
            minPublishParklotParamB.setSequence(0);
        }
        minPublishParklotParamB.setQhValue(null == minPublishInterval ? Constants.MIN_PUBLISH_INTERVAL_DEFAULT : String.valueOf(minPublishInterval));
        if (null == parklotParamsBRepository.save(minPublishParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB minSharingPeriodParklotParamB = null;
        if (isEdit) {
            minSharingPeriodParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, MIN_SHARING_PERIOD, Status.Common.VALID.getInt());
        }
        if (minSharingPeriodParklotParamB == null) {
            minSharingPeriodParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            minSharingPeriodParklotParamB.setQhKey(MIN_SHARING_PERIOD);
            minSharingPeriodParklotParamB.setSequence(1);
        }
        minSharingPeriodParklotParamB.setQhValue(null == minSharePeriod ? Constants.MIN_SHARING_PERIOD_DEFAULT : String.valueOf(minSharePeriod));
        if (null == parklotParamsBRepository.save(minSharingPeriodParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB freeCancellationTimeParklotParamB = null;
        if (isEdit) {
            freeCancellationTimeParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, FREE_CANCELLATION_TIME, Status.Common.VALID.getInt());
        }
        if (freeCancellationTimeParklotParamB == null) {
            freeCancellationTimeParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            freeCancellationTimeParklotParamB.setQhKey(FREE_CANCELLATION_TIME);
            freeCancellationTimeParklotParamB.setSequence(2);
        }
        freeCancellationTimeParklotParamB.setQhValue(null == freeCancellationTime ? Constants.FREE_CANCELLATION_TIME_DEFAULT : String.valueOf(freeCancellationTime));
        if (null == parklotParamsBRepository.save(freeCancellationTimeParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB maxDelayTimeParklotParamB = null;
        if (isEdit) {
            maxDelayTimeParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, MAX_DELAY_TIME, Status.Common.VALID.getInt());
        }
        if (null == maxDelayTimeParklotParamB) {
            maxDelayTimeParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            maxDelayTimeParklotParamB.setQhKey(MAX_DELAY_TIME);
            maxDelayTimeParklotParamB.setSequence(3);
        }
        maxDelayTimeParklotParamB.setQhValue(null == maxDelayTime ? Constants.MAX_DELAY_TIME_DEFAULT : String.valueOf(maxDelayTime));
        if (null == parklotParamsBRepository.save(maxDelayTimeParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB minChargingPeriodParklotParamB = null;
        if (isEdit) {
            minChargingPeriodParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, MIN_CHARGING_PERIOD, Status.Common.VALID.getInt());
        }
        if (null == minChargingPeriodParklotParamB) {
            minChargingPeriodParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            minChargingPeriodParklotParamB.setQhKey(MIN_CHARGING_PERIOD);
            minChargingPeriodParklotParamB.setSequence(4);
        }
        minChargingPeriodParklotParamB.setQhValue(null == minChargePeriod ? Constants.MIN_CHARGING_PERIOD_DEFAULT : String.valueOf(minChargePeriod));
        if (null == parklotParamsBRepository.save(minChargingPeriodParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB advanceChangeTimeParklotParamB = null;
        if (isEdit) {
            advanceChangeTimeParklotParamB = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, ADVANCE_CHANGE_TIME, Status.Common.VALID.getInt());
        }
        if (advanceChangeTimeParklotParamB == null) {
            advanceChangeTimeParklotParamB = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            advanceChangeTimeParklotParamB.setQhKey(ADVANCE_CHANGE_TIME);
            advanceChangeTimeParklotParamB.setSequence(5);
        }
        advanceChangeTimeParklotParamB.setQhValue(null == advanceChangeTime ? Constants.ADVANCE_CHANGE_TIME_DEFAULT : String.valueOf(advanceChangeTime));
        if (null == parklotParamsBRepository.save(advanceChangeTimeParklotParamB)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        ParklotParamsB advanceReservationTimeParklotParam = null;
        if (isEdit) {
            advanceReservationTimeParklotParam = parklotParamsBRepository.findByParklotIdAndQhKeyAndState(parklotId, ADVANCE_RESERVATION_TIME, Status.Common.VALID.getInt());
        }
        if (advanceReservationTimeParklotParam == null) {
            advanceReservationTimeParklotParam = new ParklotParamsB(parklotId, now, Status.Common.VALID.getInt(), Constants.EMPTY_STRING, Constants.TIME_CODE);
            advanceReservationTimeParklotParam.setQhKey(ADVANCE_RESERVATION_TIME);
            advanceReservationTimeParklotParam.setSequence(6);
        }
        advanceReservationTimeParklotParam.setQhValue(null == advanceReservationTime ? Constants.ADVANCE_RESERVATION_TIME_DEFAULT : String.valueOf(advanceReservationTime));
        if (null == parklotParamsBRepository.save(advanceReservationTimeParklotParam)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
    }

    /**
     * 绑定费用规则
     *
     * @param parklotInfoRequest 添加新停车区的请求
     * @param parklotId          停车区id
     */
    @Transactional(rollbackFor = Exception.class)
    protected void parklotFeeRuleBind(ParklotInfoRequest parklotInfoRequest, Integer parklotId, boolean isEdit) {
        Integer reserveFeeRuleId = parklotInfoRequest.getReserveFeeRuleId();
        Integer parkingFeeRuleId = parklotInfoRequest.getParkingFeeRuleId();
        if (null != reserveFeeRuleId && reserveFeeRuleId > Constants.EMPTY_CAPACITY) {
            ParklotFeeRuleReserveB parklotFeeRuleReserveB;
            if (isEdit) {
                parklotFeeRuleReserveB = parklotFeeRuleReserveBRepository.findByParklotIdAndState(parklotId, Status.Common.VALID.getInt());
                if (parklotFeeRuleReserveB == null) {
                    parklotFeeRuleReserveB = new ParklotFeeRuleReserveB();
                }
                parklotFeeRuleReserveB.setFeeRuleId(reserveFeeRuleId);
                parklotFeeRuleReserveB.setParklotId(parklotId);
                parklotFeeRuleReserveB.setState(Status.Common.VALID.getInt());
                parklotFeeRuleReserveB.setUpdateTime(System.currentTimeMillis());
                parklotFeeRuleReserveBRepository.save(parklotFeeRuleReserveB);
            } else {
                parklotFeeRuleReserveB = new ParklotFeeRuleReserveB(parklotId, reserveFeeRuleId, Status.Common.VALID.getInt(), System.currentTimeMillis());
            }
            if (null == parklotFeeRuleReserveBRepository.save(parklotFeeRuleReserveB)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        }
        if (null != parkingFeeRuleId && parkingFeeRuleId > Constants.EMPTY_CAPACITY) {
            ParklotFeeRuleParkingB parklotFeeRuleParkingB;
            if (isEdit) {
                parklotFeeRuleParkingB = parklotFeeRuleParkingBRepository.findByParklotIdAndState(parklotId, Status.Common.VALID.getInt());
                if (parklotFeeRuleParkingB == null) {
                    parklotFeeRuleParkingB = new ParklotFeeRuleParkingB();
                    parklotFeeRuleParkingB.setParklotId(parklotId);
                    parklotFeeRuleParkingB.setState(Status.Common.VALID.getInt());
                }
                parklotFeeRuleParkingB.setFeeRuleId(parkingFeeRuleId);
                parklotFeeRuleParkingB.setUpdateTime(System.currentTimeMillis());
            } else {
                parklotFeeRuleParkingB = new ParklotFeeRuleParkingB(parklotId, parkingFeeRuleId, Status.Common.VALID.getInt(), System.currentTimeMillis());
            }
            if (null == parklotFeeRuleParkingBRepository.save(parklotFeeRuleParkingB)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        }
    }


    /**
     * 保存停车区的信息
     *
     * @param parklotInfoRequest 添加新停车区的请求
     */
    @Transactional(rollbackFor = Exception.class)
    protected Parklot parklotSave(ParklotInfoRequest parklotInfoRequest, Integer parklotId, boolean isEdit) {
        Parklot parklot;
        if (isEdit) {
            parklot = parklotRepository.findOne(parklotId);
        } else {
            parklot = new Parklot();
        }
        parklot.setAreaId(parklotInfoRequest.getAreaId());
        parklot.setAddress(parklotInfoRequest.getAddress());
        parklot.setChargeType(parklotInfoRequest.getChargeType());
        parklot.setName(parklotInfoRequest.getName());
        parklot.setContactName(parklotInfoRequest.getContactName());
        parklot.setContactPhone(parklotInfoRequest.getContactPhone());
        final String latLng = parklotInfoRequest.getLatLng();
        parklot.setLng(Double.valueOf(latLng.split(Constants.DELIMITER_COMMA)[0]));
        parklot.setLat(Double.valueOf(latLng.split(Constants.DELIMITER_COMMA)[1]));
        final String navLatLng = parklotInfoRequest.getNaviLatLng();
        parklot.setNaviLng(Double.valueOf(navLatLng.split(Constants.DELIMITER_COMMA)[0]));
        parklot.setNaviLat(Double.valueOf(navLatLng.split(Constants.DELIMITER_COMMA)[1]));
        Integer type = parklotInfoRequest.getType();
        if (type < PARKLOT_TYPE_LIMIT) {
            parklot.setType(type);
            parklot.setInnershare(0);
        } else {
            parklot.setType(type - 2);
            parklot.setInnershare(1);
        }
        parklot.setKind(parklotInfoRequest.getKind());
        parklot.setState(parklotInfoRequest.getState());
        if (null != parklotInfoRequest.getFeeIntro()) {
            parklot.setFeeIntro(parklotInfoRequest.getFeeIntro());
        }
        parklot.setCreateTime(System.currentTimeMillis());
        parklot.setModifyTime(System.currentTimeMillis());
        parklot.setHasRelay(HasRelayMeter.NO.ordinal());
        parklot.setAllocable(Constants.PARKING_FEE_ALLOCABLE);
        if (null == (parklot = parklotRepository.save(parklot))) {
            log.error("parklot save error");
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        return parklot;
    }

    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    @EnableTenantFilter
    public Resp findOne(Integer id) {
        employeeRepository.findById(id);
        Parklot parklot = parklotRepository.findById(id);
        List<Area> areaList = areaService.findAreaList(parklot.getAreaId());
        ParkLotData parkLoctData = new ParkLotData();
        parkLoctData.setId(parklot.getId());
        parkLoctData.setContactPhone(parklot.getContactPhone());
        parkLoctData.setContactName(parklot.getContactName());
        for (int i = 0; i < areaList.size(); i++) {
            switch (i) {
                case 0:
                    parkLoctData.setProvinceId(areaList.get(i).getId());
                    break;
                case 1:
                    parkLoctData.setCityId(areaList.get(i).getId());
                    break;
                case 2:
                    parkLoctData.setAreaId(areaList.get(i).getId());
                    break;
                default:
                    break;

            }
        }
        parkLoctData.setName(parklot.getName());
        parkLoctData.setAddress(parklot.getAddress());
        parkLoctData.setType(parklot.getType());
        parkLoctData.setLat(parklot.getLat());
        parkLoctData.setLng(parklot.getLng());
        parkLoctData.setNaviLat(parklot.getNaviLat());
        parkLoctData.setNaviLng(parklot.getNaviLng());
        parkLoctData.setKind(parklot.getKind());
        parkLoctData.setState(parklot.getState());
        parkLoctData.setFeeIntro(parklot.getFeeIntro());
        parkLoctData.setChargeType(parklot.getChargeType());
        ParklotAmount parklotAmount = parklotAmountRepository.findByParklotId(parkLoctData.getId());
        if (parklotAmount != null) {
            parkLoctData.setTotalAmount(parklotAmount.getTotalAmount());
            parkLoctData.setSignedAmount(parklotAmount.getSignedAmount());
            parkLoctData.setIdleAmount(parklotAmount.getIdleAmount());
            parkLoctData.setLeftAmountType(parklotAmount.getLeftAmountType());
            parkLoctData.setLeftAmount(parklotAmount.getLeftAmount());
        }
        Integer pid = parklot.getId();
        Integer state = Status.Common.VALID.getInt();
        String parkLotValue = parklotParamsBRepository.findValueByParklotId(pid,
                Constants.ADVANCE_RESERVATION_TIME, state);
        if (StringUtils.isNotEmpty(parkLotValue)) {
            parkLoctData.setParkLotValue(parkLotValue);
        }
        List<File> fileList = fileRepository.findByParklotId(pid);
        if (fileList != null && fileList.size() > Constants.EMPTY_CAPACITY) {
            for (int i = 0; i < fileList.size(); i++) {
                final String path = fileList.get(i).getPath();
                if (StringUtils.isNotEmpty(path)) {
                    fileList.get(i).setPath(configurationFiles.getPicPath() + path);
                }
            }
        }
        parkLoctData.setFileList(fileList);
        parkLoctData.setInnershare(parklot.getInnershare());
        parkLoctData.setInnershareStr(Status.ParkingInner.find(parklot.getInnershare()));
        ParkingFeeRuleInfoData infoData = feeRuleMapper.queryParkingFeeRuleById(pid);
        List<FeeRuleReserve> feeRuleReserveList = parklotMapper.queryParklotReserveFeeRule(pid, state);
        parkLoctData.setParkingFeeRuleInfoData(infoData);
        parkLoctData.setFeeRuleReserveList(feeRuleReserveList);
        List<ParklotParamsB> parklotTimeParamsList = parklotParamsMapper.queryParklotParams(
                pid, state, Constants.TIME_CODE);
        parkLoctData.setParklotTimeParamsList(parklotTimeParamsList);
        List<ParklotParamsB> parklotParamsList = parklotParamsMapper.queryParklotParams(
                pid, state, Constants.ALLOCATION_CODE);
        parkLoctData.setParklotPlatformParamsList(parklotParamsList);
        if (parklot.getMobileUserId() != null) {
            parkLoctData.setUserMobile(userMobileRepository.findOne(parklot.getMobileUserId()));
        }
        parkLoctData.setBarrierList(barrierRepository.findByparklotId(pid, state));
        parkLoctData.setAccessList(accessRepository.findAccessByparklotId(pid, state));
        parkLoctData.setRelaymeterList(relaymeterRepository.findRelaymeterByparklotId(pid, state));

        // 优惠套餐关联
        DiscountPackageData packageByParkLot = discountPackageMapper.findParklotPackageByParkId(id);
        parkLoctData.setPackageByParkLot(packageByParkLot);

        return RespUtil.successResp(parkLoctData);
    }

    @Transactional(rollbackFor = Exception.class)
    protected void saveAdmin(Parklot parklot, ParklotInfoRequest request, boolean isEdit) {
        log.info("更新停车区管理员");
        final String adminPhone = request.getAdminPhone();
        if (!PhoneFormatCheckUtils.isPhoneLegal(adminPhone)) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
        UserMobile user;
        if (isEdit) {
            String lastAdminPhone = request.getLastAdminPhone();
            if (!PhoneFormatCheckUtils.isPhoneLegal(lastAdminPhone)) {
                throw new QhieWebException(Status.WebErr.PARAM_ERROR);
            }
            if (!lastAdminPhone.equals(adminPhone) && null != userMobileRepository.findByPhone(adminPhone)) {
                throw new QhieWebException(Status.WebErr.PHONE_NUM_EXISTS);
            }
            user = userMobileRepository.findByPhone(lastAdminPhone);
        } else {
            user = userMobileRepository.findByPhone(adminPhone);
        }
        if (user == null) {
            user = UserMobile.autoSignup(request.getAdminName(), adminPhone);
            /*
             * 初始化user表
             */
            val userNew = userMobileRepository.save(user);
            /*
             * 初始化三方信息表
             */
            saveUserExtraInfo(userNew);
            /*
             * 初始化金额表
             */
            saveBalance(userNew);
            log.info("new user: {}", userNew);
            parklot.setMobileUserId(userNew.getId());
            return;
        } else {
            user.setName(request.getAdminName());
            user.setPhone(adminPhone);
            user.setType(Status.userType.USERTYPE_TWO.getValue().byteValue());
            if (null == userMobileRepository.save(user)) {
                throw new QhieWebException(Status.WebErr.INSERT_ERROR);
            }
        }
        val parklotList = parklotRepository.findIdsByMobileUserId(user.getId());
        if (parklotList != null && parklotList.size() > Constants.EMPTY_CAPACITY) {
            if (parklot.getId() != null) {
                if (parklotList.stream().noneMatch((n) -> n.equals(parklot.getId()))) {
                    throw new QhieWebException(Status.WebErr.OWNER_CANNOT_BE_AN_ADMINISTRATOR);
                }
            } else {
                throw new QhieWebException(Status.WebErr.OWNER_CANNOT_BE_AN_ADMINISTRATOR);
            }

        }
        if (!request.getAdminPhone().equals(request.getLastAdminPhone())) {
            List<Integer> states = new ArrayList<>();
            states.add(Status.OrderParking.UNCONFIRMED.getInt());
            states.add(Status.OrderParking.USED.getInt());
            states.add(Status.OrderParking.UNPAID.getInt());
            Integer unfinishedOrderCount = orderParkingRepository.findOrderParkingByMobileUserIdAndStates(user.getId(), states, Status.OrderParking.RESERVED.getInt());
            Integer unfinishedRefund = orderRefundRepository.findByUserIdAndState(user.getId(), Status.Refund.PROCESSING.getInt());
            Integer unfinishedWithdraw = orderWithdrawRepository.countByUserAndState(user.getId(), Status.Withdraw.PROCESSING.getInt());
            log.info("order {}, refund {}, withdraw {}", unfinishedOrderCount, unfinishedRefund, unfinishedWithdraw);
            if ((unfinishedOrderCount + unfinishedRefund + unfinishedWithdraw) > Constants.EMPTY_CAPACITY) {
                throw new QhieWebException(Status.WebErr.USER_WITH_ONGOING_ORDERS_CANNOT_BE_AN_ADMINISTRATOR);
            }
            val balance = balanceUserRepository.findByMobileUserIdAndState(user.getId(), Status.Common.VALID.getInt());
            if (balance != null) {
                log.info("balance {}", balance);
                if (balance.getBalanceEarn().compareTo(Constants.BIGDECIMAL_ZERO) > Constants.EMPTY_CAPACITY) {
                    throw new QhieWebException(Status.WebErr.USER_WITH_AVAILABLE_BALANCE_CANNOT_BE_AN_ADMINISTRATOR);
                }
                if (balance.getBalanceInvoice().compareTo(Constants.BIGDECIMAL_ZERO) > Constants.EMPTY_CAPACITY) {
                    throw new QhieWebException(Status.WebErr.USER_WITH_AVAILABLE_INVOICE_CANNOT_BE_AN_ADMINISTRATOR);
                }
            }
        }
        log.info("停车区管理员id是{}", user.getId());
        parklot.setMobileUserId(user.getId());
        if (null == (parklotRepository.save(parklot))) {
            throw new QhieWebException(Status.WebErr.PARAM_ERROR);
        }
    }

    private void saveBalance(UserMobile userNew) {
        BalanceUser balanceUser = new BalanceUser();
        balanceUser.setMobileUserId(userNew.getId());
        balanceUser.setState(Status.Common.VALID.getInt());
        balanceUserRepository.save(balanceUser);
    }

    private void saveUserExtraInfo(UserMobile userNew) {
        UserExtraInfo userExtraInfo = new UserExtraInfo();
        userExtraInfo.setMobileUserId(userNew.getId());
        userExtraInfo.setJpushRegId(Constants.EMPTY_STRING);
        userExtraInfoRepository.save(userExtraInfo);
    }

    @Override
    public Resp getAdmin(Integer id) {
        ParklotAdminData parklotAdminData = new ParklotAdminData();
        Parklot parklot = parklotRepository.findOne(id);
        if (null == parklot || null == parklot.getMobileUserId()) {
            throw new QhieWebException(Status.WebErr.PARAMS_PARKLOT_ADMIN_ERROR);
        }
        UserMobile user = userMobileRepository.findOne(parklot.getMobileUserId());
        parklotAdminData.setUserId(user.getId());
        parklotAdminData.setPhone(user.getPhone());
        return RespUtil.successResp(parklotAdminData);
    }

    @Override
    @Transactional
    public Resp deleteFile(ParklotFileRemoveRequest parklotFileRemoveRequest) {
        final Integer fileId = parklotFileRemoveRequest.getFileId();
        final Integer parklotId = parklotFileRemoveRequest.getParklotId();
        int deleteRows = parklotFileBRepository.updateParklotFileBState(parklotId, fileId, Status.Common.INVALID.getInt());
        log.info("delete {} rows", deleteRows);
        return RespUtil.successResp();
    }

    @Override
    @EnableTenantFilter
    @AddTenantInfo
    public Resp query(ParklotRequest request) {
        AbstractPaged<Parklot> ab = queryFunction.query(request, where(request)).getData();
        return RespUtil.successResp(ab);
    }

    @Override
    @EnableTenantFilter
    public Resp excel(ParklotRequest request, OutputStream outputStream) throws IOException {
        List<Parklot> parklots = parklotRepository.findAll(where(request));
        ExcelUtil<Parklot> excel = new ExcelUtil<>(outputStream, Parklot.class);
        excel.buildFormat("type", Status.ParklotType::find);
        excel.buildFormat("state", Status.Common::find);
        excel.buildFormat("kind", Status.ParkingKind::find);
        excel.setPropertyFormat(this::reverseLookup);
        excel.write(parklots.stream().map(this::transientProperty).collect(toList()));
        return RespUtil.successResp();
    }

    @Override
    public void downloadAllQr(HttpServletRequest request, HttpServletResponse response, String parklotIds) throws Exception {
        String str[]=parklotIds.split(",");
        String token = wxService.replaceXcxAccessToken();
        List<String> files=new ArrayList<>();
        int taskSize = str.length;
        // 创建一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        //并发任务计数器，可以阻塞等待所有任务完成
        CountDownLatch countDownLatch = new CountDownLatch(taskSize);

        for(String ids:str){
            // 执行任务并获取Future对象
            Future f = pool.submit(()->{
                try {
                    Map<String, Object> params = new HashMap<>();
                    params.put("path", "/pages/index/index?parklotId=" + ids);
                    params.put("width", 800);
                    CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                    HttpPost httpPost = new HttpPost("https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?access_token=" + token);  // 接口
                    httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
                    String body = net.sf.json.JSONObject.fromObject(params).toString();           //必须是json模式的 post
                    StringEntity entity;
                    entity = new StringEntity(body);
                    entity.setContentType("image/png");
                    httpPost.setEntity(entity);
                    HttpResponse responses;
                    responses = httpClient.execute(httpPost);
                    InputStream inputStream = responses.getEntity().getContent();
                    log.info("ids="+Integer.parseInt(ids));
                    Parklot parklot=parklotRepository.findOne(Integer.parseInt(ids));
                    String name = parklot.getName()+"_"+parklot.getId() + ".png";
                    java.io.File targetFile = new java.io.File(configurationFiles.getWebUploadPath() + "wx/");
                    if (!targetFile.exists()) {
                        targetFile.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(configurationFiles.getWebUploadPath() + "wx/" + name);
                    byte[] buffer = new byte[8192];
                    int bytesRead = 0;
                    while ((bytesRead = inputStream.read(buffer, 0, 8192)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    //ArrayList线程不安全，加锁同步
                    synchronized(files) {
                        files.add(configurationFiles.getPicPath() + "wx/" + name + "," + name);
                    }
                    out.flush();
                    out.close();
                }catch (Exception e){
                    log.error(e.getMessage());
                }finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        // 关闭线程池
        pool.shutdown();
        String downloadFilename = System.currentTimeMillis()+".zip";//文件的名称
        downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
        response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
        response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        for (int i=0;i<files.size();i++) {
            String[] strs=files.get(i).split(",");
            URL url = new URL(strs[0]);
            zos.putNextEntry(new ZipEntry(strs[1]));
            //FileInputStream fis = new FileInputStream(new File(files[i]));
            InputStream fis = url.openConnection().getInputStream();
            byte[] buffer = new byte[1024];
            int r = 0;
            while ((r = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, r);
            }
            fis.close();
        }
        zos.flush();
        zos.close();
    }

    private void reverseLookup(Map<String, Object> map) {
        List<ParklotParamsB> parklotTimeParamsList = parklotParamsMapper.queryParklotParams(
                (Integer) map.get("id"), Status.Common.VALID.getInt(), Constants.TIME_CODE);
        parklotTimeParamsList.stream()
                .filter(param -> "advance_reservation_time".equalsIgnoreCase(param.getQhKey()))
                .findAny()
                .ifPresent(parklotParamsB -> map.put("reserveTime", parklotParamsB.getQhValue()));
        val parklotFeeParamList = parklotParamsMapper.queryParklotParams(
                (Integer) map.get("id"), Status.Common.VALID.getInt(), Constants.ALLOCATION_CODE);
        Map<String, String> feeMap = new HashMap<>();
        parklotFeeParamList.forEach(param -> feeMap.put(param.getQhKey(), param.getQhValue()));
        StringBuilder describe = new StringBuilder();
        describe.append("平台：").append(feeMap.get("platform_percentage")).append(";");
        describe.append("业主：").append(feeMap.get("owner_percentage")).append(":");
        describe.append("物业：").append(feeMap.get("propcomp_percentage")).append(":");
        describe.append("运营商：").append(feeMap.get("operator_percentage"));
        map.put("allocation", describe);
        map.put("lnglat", map.get("lng") + "," + map.get("lat"));
        map.put("navLnglat", map.get("naviLng") + "," + map.get("naviLat"));
        if (map.get("mobileUserId") != null) {
            val use = userMobileRepository.findOne((Integer) map.get("mobileUserId"));
            if (use != null) {
                map.put("adminPhone", use.getPhone());
                map.put("adminName", use.getName());
            }
        }
    }

    private Specification<Parklot> where(ParklotRequest request) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<Predicate>();
            val page = new PageableUtil(root, query, cb);
            page.like("name", request.getName());
            page.equal("type", request.getType());
            page.like("id", request.getId());
            if (request.getAreaId() != null && request.getCityId() != null && request.getProvinceId() != null) {
                page.equal("areaId", request.getAreaId());
            }
            if (request.getProvinceId() != null && request.getCityId() != null && request.getAreaId() == null) {
                val ids = areaRepository.findByParentTwoId(request.getCityId());
                page.in("areaId", ids);
            }
            if (request.getAreaId() == null && request.getCityId() == null && request.getProvinceId() != null) {
                val ids = areaRepository.findByParentOneId(request.getProvinceId());
                page.in("areaId", ids);
                ;
            }
            if (request.getInnershare() != null) {
                page.equal("innershare", request.getInnershare());
            }
            return page.pridect();
        };
    }
}
