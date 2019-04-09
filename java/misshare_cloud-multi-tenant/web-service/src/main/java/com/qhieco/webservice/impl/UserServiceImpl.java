package com.qhieco.webservice.impl;

import com.qhieco.commonentity.UserExtraInfo;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonrepo.RoleRepository;
import com.qhieco.commonrepo.UserExtraInfoRepository;
import com.qhieco.commonrepo.UserMobileRepository;
import com.qhieco.commonrepo.UserWebRespository;
import com.qhieco.config.ConfigurationFiles;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.mapper.UserMapper;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.UserData;
import com.qhieco.util.ExcelUtil;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.UserService;
import com.qhieco.webservice.exception.ExcelException;
import lombok.val;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 19:52
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    com.qhieco.webmapper.UserMapper userMapper;
    @Autowired
    UserWebRespository userWebRespository;
    @Autowired
    UserMobileRepository userMobileRepository;
    @Autowired
    private ConfigurationFiles configurationFiles;
    @Autowired
    private UserExtraInfoRepository userExtraInfoRepository;



    @Override
    public Resp<AbstractPaged<UserData>> pageUser(UserRequest userRequest) {
        if(userRequest.getState()==null){
            userRequest.setState(Status.Common.VALID.getInt());
        }
        List<UserData> userList=userMapper.pageUser(userRequest);
        Integer count=userMapper.pageUserTotalCount(userRequest);
        for(int i=0;i<userList.size();i++){
            UserData userData=userList.get(i);
            userData.setPlateNumber(userMapper.findUserPlateNumber(userData.getId()));
            if(StringUtils.isNotEmpty(userData.getPath())){
                userData.setPath(configurationFiles.getPicPath()+userData.getPath());
            }
            userList.set(i,userData);
        }
        AbstractPaged<UserData> data = AbstractPaged.<UserData>builder().sEcho
                (userRequest.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(userList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public Resp<AbstractPaged<UserData>> pageUserDetailed(UserRequest userRequest) {
        if(userRequest.getState()==null){
            userRequest.setState(Status.Common.VALID.getInt());
        }
        List<UserData> userList=userMapper.pageUserDetailed(userRequest);
        Integer count=userMapper.pageUserDetailedTotalCount(userRequest);
        AbstractPaged<UserData> data = AbstractPaged.<UserData>builder().sEcho
                (userRequest.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(userList).build();
        return RespUtil.successResp(data);
    }

    @Override
    public Resp excel(UserRequest userRequest, OutputStream outputStream, Class cl)throws IOException {
        if(userRequest.getState()==null){
            userRequest.setState(Status.Common.VALID.getInt());
        }
        List<UserData> userDataList=userMapper.excelUser(userRequest);
        if(userDataList!=null && userDataList.size()== Constants.EMPTY_CAPACITY){
            throw new ExcelException(Status.WebErr.EMPTY_EXCEL.getCode(),Status.WebErr.EMPTY_EXCEL.getMsg());
        }
        for(int i=0;i<userDataList.size();i++){
            StringBuffer sb=new StringBuffer();
            String stateStr=Status.Common.find(userDataList.get(i).getState());
            String userTypeStr=Status.userType.find(userDataList.get(i).getUserType());
            userDataList.get(i).setStateStr(stateStr);
            userDataList.get(i).setUserTypeStr(userTypeStr);
           List<String> str= userMapper.findUserPlateNumber(userDataList.get(i).getId());
           for(String s:str){
               sb.append(s+",");
           }
           userDataList.get(i).setPlateNumberStr(sb.toString());
        }
        ExcelUtil<UserData> userDataExcelUtil=new ExcelUtil<>(outputStream,UserData.class);
        userDataExcelUtil.write(userDataList);
        return RespUtil.successResp();
    }

    @Override
    public Resp<UserData> findUserOne(Integer id) {
        UserRequest userRequest=new UserRequest();
        userRequest.setState(Status.Common.VALID.getInt());
        userRequest.setId(id);
        UserData userData= userMapper.findUserOne(userRequest);
        if(userData==null){
            return RespUtil.errorResp(Status.WebErr.ENTITY_NOT_EXISTS.getCode(),
                    Status.WebErr.ENTITY_NOT_EXISTS.getMsg());
        }
        userData.setPlateNumber(userMapper.findUserPlateNumber(userData.getId()));
        return RespUtil.successResp(userData);
    }

    @Override
    public Resp editUser(UserRequest userRequest) {
        UserMobile userMobile=userMobileRepository.findOne(userRequest.getId());
        userMobile.setIdentityNumber(userRequest.getIdentityNumber());
        userMobile.setName(userRequest.getName());
        userMobileRepository.save(userMobile);
        return RespUtil.successResp();
    }

    @Override
    public Resp untieWxUser(Integer userId) {
        UserExtraInfo userExtraInfo=userExtraInfoRepository.findByUserExtraInfoId(userId);
        userExtraInfo.setWxUnionId(null);
        userExtraInfo.setWxBindOpenId(null);
        userExtraInfoRepository.save(userExtraInfo);
        return RespUtil.successResp();
    }
}
