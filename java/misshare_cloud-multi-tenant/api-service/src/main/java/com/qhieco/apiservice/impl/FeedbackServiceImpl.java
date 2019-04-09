package com.qhieco.apiservice.impl;

import com.qhieco.apiservice.FeedbackService;
import com.qhieco.apiservice.exception.QhieException;
import com.qhieco.commonentity.Feedback;
import com.qhieco.commonentity.File;
import com.qhieco.commonentity.relational.FeedbackProblemB;
import com.qhieco.commonrepo.FeedbackProblemBRepository;
import com.qhieco.commonrepo.FeedbackRepository;
import com.qhieco.commonrepo.FileRepository;
import com.qhieco.commonrepo.ProblemRepository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.api.FeedbackRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.ProblemResp;
import com.qhieco.util.CommonUtil;
import com.qhieco.util.Md5Util;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/7 下午12:21
 * <p>
 * 类说明：
 *     地区Service的实现类
 */
@Service
@Slf4j
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    ProblemRepository problemRepository;

    @Autowired
    FeedbackRepository feedbackRepository;

    @Autowired
    FeedbackProblemBRepository feedbackProblemBRepository;

    @Autowired
    ConfigurationFiles configurationFiles;

    @Autowired
    FileRepository fileRepository;

    @Override
    public Resp problemAll(FeedbackRequest feedbackRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(feedbackRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        ProblemResp problemResp = new ProblemResp();
        problemResp.setProblems(problemRepository.findByState(Status.Common.VALID.getInt()));
        return RespUtil.successResp(problemResp);
    }

    @Override
    @Transactional
    public Resp add(FeedbackRequest feedbackRequest){
        //验证时间戳是否合法
        if (CommonUtil.isTimeStampInValid(feedbackRequest.getTimestamp())) {
            throw new QhieException(Status.ApiErr.TIMESTAMP_ERROR);
        }
        Long now = System.currentTimeMillis();
        List<MultipartFile> file = feedbackRequest.getFile();
        Integer userId = feedbackRequest.getUser_id();
        Integer parklotId = feedbackRequest.getParklot_id();
        Integer[] problemIds = feedbackRequest.getProblem_ids();
        String problemIdsStr = feedbackRequest.getProblem_ids_str();
        String remark = feedbackRequest.getRemark();
        StringBuilder fileIds = new StringBuilder();
        // 保存文件的路径，多个文件使用逗号,隔开
        String savePath = configurationFiles.getWebUploadPath().concat(Constants.DIRECTORY_FEEDBACK);
        log.info("上传文件保存路径 savePath=" + savePath);
        if (null != file && file.size() > Constants.EMPTY_CAPACITY) {
            for (MultipartFile fileCurr : file) {
                String fileName = Md5Util.getFileName(fileCurr.getOriginalFilename());
                String filePath = savePath + fileName;
                log.info("file path is {}", filePath);
                if (writeFileToDisk(fileCurr, filePath)) {
                    return RespUtil.errorResp(Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getCode(), Status.ApiErr.FILE_SAVE_MODIFY_ERROR.getMsg());
                }
                filePath = Constants.DIRECTORY_FEEDBACK + fileName;
                File feedbackFile = saveFile(fileName, filePath);
                fileIds.append(feedbackFile.getId()).append(Constants.DELIMITER_COMMA);
            }
            // 去掉最后面的逗号
            fileIds = fileIds.deleteCharAt(fileIds.length() - 1);
            log.info("上传文件id" + fileIds);
        } else {
            log.info("没有上传图片");
        }
        Feedback feedback = new Feedback(userId, parklotId, remark, now, fileIds.toString());
        Feedback feedbackResp = feedbackRepository.save(feedback);

        if ((problemIds == null || problemIds.length == 0) && org.apache.commons.lang.StringUtils.isNotEmpty(problemIdsStr)) {
            String[] proArr = problemIdsStr.split(Constants.DELIMITER_COMMA);
            problemIds = new Integer[proArr.length];
            for (int i = 0; i < proArr.length; i++) {
                if(StringUtils.isEmpty(proArr[i])){
                    continue;
                }
                problemIds[i] = Integer.valueOf(proArr[i]);
            }
        }
        for(Integer problemId : problemIds) {
            FeedbackProblemB feedbackProblemB = new FeedbackProblemB(feedbackResp.getId(), problemId, now, now, Status.Common.VALID.getInt());
            feedbackProblemBRepository.save(feedbackProblemB);
        }
        return RespUtil.successResp();
    }

    @Transactional(rollbackFor = Exception.class)
    protected File saveFile(String fileName, String filePath) {
        File feedbackFile = new File();
        feedbackFile.setName(fileName);
        feedbackFile.setPath(filePath);
        feedbackFile.setIntro(Constants.DIRECTORY_FEEDBACK);
        feedbackFile.setCreateTime(System.currentTimeMillis());
        feedbackFile.setState(Status.Common.VALID.getInt());
        if (null == (feedbackFile = fileRepository.save(feedbackFile))) {
            throw new QhieException(Status.ApiErr.INSERT_ERROR);
        }
        return feedbackFile;
    }

    private boolean writeFileToDisk(MultipartFile file, String filePath) {
        log.info("write file into disk");
        try {
            byte[] fileBytes;
            fileBytes = file.getBytes();
            Path path = Paths.get(filePath);
            Files.write(path, fileBytes);
        } catch (IOException e) {
            return true;
        }
        log.info("finish write file");
        return false;
    }

}
