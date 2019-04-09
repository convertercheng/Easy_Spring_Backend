package com.qhieco.webservice.impl;

import com.qhieco.commonentity.ApplyParkloc;
import com.qhieco.commonentity.Message;
import com.qhieco.commonentity.UserExtraInfo;
import com.qhieco.commonrepo.ApplyParklocRepository;
import com.qhieco.commonrepo.AreaRepository;
import com.qhieco.commonrepo.MessageRepository;
import com.qhieco.commonrepo.UserExtraInfoRepository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.push.QhMessageTemplate;
import com.qhieco.push.QhMessageType;
import com.qhieco.request.web.ApplyParklocRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.ApplyParklocData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.QhPushUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webmapper.ApplyParklocMapper;
import com.qhieco.webservice.ApplyService;
import com.qhieco.webservice.exception.ExcelException;
import com.qhieco.webservice.exception.QhieWebException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午8:08
 * <p>
 * 类说明：
 *     申请工单逻辑层
 */
@Service
public class ApplyServiceImpl implements ApplyService {

    @Autowired
    private ApplyParklocRepository applyParklocRepository;

    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;

    @Autowired
    private ApplyParklocMapper applyParklocMapper;

    @Autowired
    private AreaRepository areaRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Resp execl(ApplyParklocRequest applyParklocRequest, OutputStream outputStream, Class cl) throws IOException {
        List<ApplyParklocData> applyParklocDataList=applyParklocMapper.excelApply(applyParklocRequest);
        if(applyParklocDataList!=null && applyParklocDataList.size()==Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(int i=0;i<applyParklocDataList.size();i++){
            applyParklocDataList.get(i).setStateStr(Status.Apply.find(applyParklocDataList.get(i).getState()));
        }
        ExcelUtil<ApplyParklocData> excelUtil = new ExcelUtil<>(outputStream,ApplyParklocData.class);
        excelUtil.write(applyParklocDataList);
        return RespUtil.successResp();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Resp processParkloc(ApplyParklocRequest applyParklocRequest) {
        Long now = System.currentTimeMillis();
        Integer applyId = applyParklocRequest.getApplyId();
        Integer webUserId = applyParklocRequest.getWebUserId();
        Integer state = applyParklocRequest.getState();
        String message = applyParklocRequest.getMessage();
        ApplyParkloc applyParkloc = applyParklocRepository.findOne(applyId);
        applyParkloc.setWebUserId(webUserId);
        applyParkloc.setMessage(message);
        applyParkloc.setCompleteTime(now);
        applyParkloc.setState(state);
        if (null == applyParklocRepository.save(applyParkloc)) {
            throw new QhieWebException(Status.WebErr.INSERT_ERROR);
        }
        UserExtraInfo userExtraInfo = userExtraInfoRepository.findByMobileUserId(applyParkloc.getMobileUserId());
        String content = "";
        String placeHolder = "";
        if(Status.Apply.PROCESS_SUCCESS.getInt().equals(state)){
            content = QhMessageTemplate.APPLY_PARKLOC_SUCCESS;
            placeHolder = applyParkloc.getParklotName();
        }
        else if(Status.Apply.PROCESS_FAILED.getInt().equals(state)){
            content = message;
        }
        //极光推送
        if(null != userExtraInfo && !StringUtils.isEmpty(userExtraInfo.getJpushRegId())){
            QhPushUtil.getInstance().sendQhPush(userExtraInfo.getJpushRegId(), QhMessageType.WALLET, content,placeHolder);
        }
        if(!"".equals(placeHolder)){
            content = String.format(content,placeHolder);
        }
        Message message1 = new Message(applyParkloc.getMobileUserId(),webUserId,QhMessageType.WALLET.getTitle(),content,null,Constants.MESSAGE_TYPE_PERSONAL,Constants.MESSAGE_KIND_JPUSH,Status.Common.VALID.getInt(),now);
        messageRepository.save(message1);
        return RespUtil.successResp();
    }

    @Override
    public Resp<AbstractPaged<ApplyParklocData>> query(ApplyParklocRequest applyParklocRequest) {
        List<Integer> id=new ArrayList<Integer>();
        if(applyParklocRequest.getAreaId()!=null && applyParklocRequest.getCityId()!=null && applyParklocRequest.getProvinceId()!=null){
            id.add(applyParklocRequest.getAreaId());
        }
        if(applyParklocRequest.getProvinceId()!=null && applyParklocRequest.getCityId()!=null && applyParklocRequest.getAreaId()==null){
                id= areaRepository.findByParentTwoId(applyParklocRequest.getCityId());
        }
        if(applyParklocRequest.getAreaId()==null && applyParklocRequest.getCityId()==null && applyParklocRequest.getProvinceId()!=null){
                id= areaRepository.findByParentOneId(applyParklocRequest.getProvinceId());
        }
        applyParklocRequest.setIds(id);
        List<ApplyParklocData> applyDataList = applyParklocMapper.pageApply(applyParklocRequest);
        Integer count = applyParklocMapper.pageApplyTotalCount(applyParklocRequest);
        AbstractPaged<ApplyParklocData> data = AbstractPaged.<ApplyParklocData>builder().sEcho
                (applyParklocRequest.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(applyDataList).build();
        return RespUtil.successResp(data);
    }
}
