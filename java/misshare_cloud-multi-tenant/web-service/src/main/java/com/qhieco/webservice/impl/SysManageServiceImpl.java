package com.qhieco.webservice.impl;

import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.FeekbackRequest;
import com.qhieco.request.web.LogOperationWebRequest;
import com.qhieco.request.web.LoginLogRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.*;
import com.qhieco.webservice.SysManageService;
import com.qhieco.webservice.exception.ExcelException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 10:56
 * <p>
 * 类说明：
 * ${说明}
 */
@Service
@Slf4j
public class SysManageServiceImpl implements SysManageService {
    @Autowired
    private FeekBackMapper feekBackMapper;

    @Autowired
    private LogOperateMapper logOperateMapper;

    @Autowired
    private LogLoginMapper logLoginMapper;

    @Autowired
    private LogOperateWebMapper logOperateWebMapper;

    @Autowired
    private FileMapper fileMapper;

    @Autowired
    private ConfigurationFiles configurationFiles;

    @Override
    public AbstractPaged<FeedBackData> queryFeekbackList(String phone, Long startTime, Long endTime, List<Integer> proIdList,
                                                         Integer startPage, Integer pageSize, Integer sEcho) {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("phone", phone);
        params.put("startTime", startTime);
        params.put("endTime", endTime);
        params.put("proIdList", proIdList);
        params.put("startPage", startPage);
        params.put("pageSize", pageSize);
        log.info(" 查询意见反馈列表的参数 params = " + params);
        int count = feekBackMapper.queryFeekbackCountByCodition(params);
        List<FeedBackData> feekBackDataList = null;
        if (count > 0) {
            feekBackDataList = feekBackMapper.queryFeekbackListByCondition(params);
            for(int i=0;i<feekBackDataList.size();i++) {
                FeedBackData feekBackData = feekBackDataList.get(i);
                if (StringUtils.isNotEmpty(feekBackData.getFileIds())) {
                    String[] fileIds = feekBackData.getFileIds().split(",");
                    List<String> filePaths = fileMapper.queryFilepathsByIds(fileIds);
                    List<String> fileAllPaths = new ArrayList<>();
                    for (int j = 0, len = filePaths.size(); j < len; j++) {
                        fileAllPaths.add(configurationFiles.getPicPath() + filePaths.get(j));
                    }
                    feekBackData.setFilePaths(fileAllPaths);
                }

            }
        }
        AbstractPaged<FeedBackData> data = AbstractPaged.<FeedBackData>builder()
                .sEcho(sEcho+1)
                .dataList(feekBackDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public Resp queryFeedbackListByConditionExcel(FeekbackRequest feekbackRequest, OutputStream outputStream)throws IOException {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("phone", feekbackRequest.getPhone());
        params.put("startTime", feekbackRequest.getStartTime());
        params.put("endTime", feekbackRequest.getEndTime());
        params.put("proIdList", feekbackRequest.getProIdList());
        int count = feekBackMapper.queryFeekbackCountByCodition(params);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }

        List<FeedBackData> feedBackDataList=feekBackMapper.queryFeedbackListByConditionExcel(feekbackRequest);
        ExcelUtil<FeedBackData> excelUtil = new ExcelUtil<>(outputStream,FeedBackData.class);
        excelUtil.write(feedBackDataList);
        return  RespUtil.successResp();
    }

    @Override
    public AbstractPaged<LogOperationData> queryLogOperateList(Integer startPage, Integer pageSize,Integer sEcho) {
        int count = logOperateMapper.queryLogOperateCount(Status.LogOperateType.TYPE_LOGIN.getInt());
        List<LogOperationData> logOperationDataList = null;
        if (count > 0) {
            logOperationDataList = logOperateMapper.queryLogOperateListByCondition(Status.LogOperateType.TYPE_LOGIN.getInt(),
                    startPage, pageSize);
        }

        AbstractPaged<LogOperationData> data = AbstractPaged.<LogOperationData>builder()
                .sEcho(sEcho+1)
                .dataList(logOperationDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public AbstractPaged<LogLoginData> queryLogLoginList(String phone, String phoneModel, Long loginStartTime, Long loginEndTime,
                                                         Integer startPage, Integer pageSize,Integer sEcho) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phone);
        params.put("phoneModel", phoneModel);
        params.put("loginStartTime", loginStartTime);
        params.put("loginEndTime", loginEndTime);
        params.put("startPage", startPage);
        params.put("pageSize", pageSize);
        params.put("logLoginType", Status.LogOperateType.TYPE_LOGIN.getInt());

        int count = logLoginMapper.queryLogLoginCount(params);
        List<LogLoginData> logLoginDataList = null;
        if (count > 0) {
            logLoginDataList = logLoginMapper.queryLogLoginListByCondition(params);
        }

        AbstractPaged<LogLoginData> data = AbstractPaged.<LogLoginData>builder()
                .sEcho(sEcho+1)
                .dataList(logLoginDataList)
                .iTotalRecords(count)
                .iTotalDisplayRecords(count)
                .build();
        return data;
    }

    @Override
    public AbstractPaged<LogOperationWebData> queryOperationWebList(LogOperationWebRequest logOperationWebRequest) {
        int count = logOperateWebMapper.queryOperationWebCount(logOperationWebRequest);
        List<LogOperationWebData> list = null;
        if (count > 0) {
            list = logOperateWebMapper.queryOperationWebList(logOperationWebRequest);
        }
        AbstractPaged<LogOperationWebData> data = AbstractPaged.<LogOperationWebData>builder().sEcho
                (logOperationWebRequest.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(list).build();
        return data;
    }

    @Override
    public Resp operationWebListExcel(LogOperationWebRequest logOperationWebRequest,OutputStream outputStream)throws IOException {
        int count = logOperateWebMapper.queryOperationWebCount(logOperationWebRequest);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }

        List<LogOperationWebData> list = logOperateWebMapper.operationWebListExcel(logOperationWebRequest);
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setStateStr(Status.Common.find(list.get(i).getState()));
        }
        ExcelUtil<LogOperationWebData> excelUtil = new ExcelUtil<>(outputStream, LogOperationWebData.class);
        excelUtil.write(list);
        return RespUtil.successResp();
    }

    @Override
    public Resp queryLogLoginExcel(LoginLogRequest loginLogRequest,OutputStream outputStream)throws IOException {
        HashMap<String, Object> params = new HashMap<>(16);
        params.put("phone", loginLogRequest.getPhone());
        params.put("phoneModel", loginLogRequest.getPhoneModel());
        params.put("loginStartTime", loginLogRequest.getLoginStartTime());
        params.put("loginEndTime", loginLogRequest.getLoginEndTime());
        params.put("logLoginType", Status.LogOperateType.TYPE_LOGIN.getInt());
        int count = logLoginMapper.queryLogLoginCount(params);
        if (count == Constants.EMPTY_CAPACITY) {
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(), Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        if (count > Constants.EXCEL_SIZE) {
            throw new ExcelException(Status.WebErr.EXCEL_DATA_TOOBIG.getCode(), Status.WebErr.EXCEL_DATA_TOOBIG.getMsg());
        }
        List<LogLoginData> list=logLoginMapper.queryLogLoginListExcel(params);

        ExcelUtil<LogLoginData> excelUtil = new ExcelUtil<LogLoginData>(outputStream,LogLoginData.class);
        excelUtil.write(list);
        return RespUtil.successResp();
    }
}
