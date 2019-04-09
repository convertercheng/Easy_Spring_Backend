package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.ParklotBItemService;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.ParkLotData;
import com.qhieco.util.PhoneFormatCheckUtils;
import com.qhieco.util.RespUtil;
import com.qhieco.webbitemmapper.ParklotBItemMapper;
import com.qhieco.webbitemmapper.UserBItemWebMapper;
import com.qhieco.webservice.exception.ExcelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/4 15:06
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class ParklotBItemServiceImpl implements ParklotBItemService {

    @Autowired
    ParklotBItemMapper parklotBItemMapper;

    @Autowired
    UserBItemWebMapper userBItemWebMapper;

    /**
     * 根据条件获取所有可用的小区列表
     * @param request
     * @return
     */
    @Override
    public Resp<List<ParkLotData>> findAllUsableParklot(ParklotRequest request) {
        if (request.getState() == null) {
            request.setState(Status.Common.VALID.getInt());
        }
        List<ParkLotData> userList = parklotBItemMapper.findAllUsableParklot(request);
        return RespUtil.successResp(userList);
    }

    /**
     * 获取分配小区的所有列表
     * @param request
     * @return
     */
    @Override
    public Resp<List<ParkLotData>> findByUserAllParklot(ParklotRequest request) {
        // 获取用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer userId = userBItemWebMapper.getUserByUserName(username);
        if(userId != null && userId !=0){
            request.setUserId(userId);
        }else{
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(),Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        List<ParkLotData> userList = parklotBItemMapper.findByUserAllParklot(request);
        return RespUtil.successResp(userList);
    }

    /**
     * 分页获取小区和停车场管理员信息
     * @param request
     * @return
     */
    @Override
    public Resp<List<ParkLotData>> findParklotAdminByPage(ParklotRequest request) {
        // 获取用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer userId = userBItemWebMapper.getUserByUserName(username);
        if(userId != null && userId !=0){
            request.setUserId(userId);
        }else{
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(),Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        List<ParkLotData> userList = parklotBItemMapper.findParklotAdminByPage(request);
        Integer count = parklotBItemMapper.pageAdminUserTotalCount(request);

        AbstractPaged<ParkLotData> data = AbstractPaged.<ParkLotData>builder().sEcho
                (request.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(userList).build();
        return RespUtil.successResp(userList);
    }

    /**
     * 根据小区获取管理员详细
     * @param request
     * @return
     */
    @Override
    public Resp<ParkLotData> findAdminUserDetailed(ParklotRequest request) {
        ParkLotData parkLotData = parklotBItemMapper.findAdminUserDetailed(request);
        return RespUtil.successResp(parkLotData);
    }

    /**
     * 更新小区管理员信息
     * @param request
     * @return
     */
    @Override
    public Resp updateAdminUserDetailed(ParklotRequest request) {
        boolean bool = PhoneFormatCheckUtils.isPhoneLegal(request.getAdminPhone());
        if(!bool){
            // 手机号码格式有误
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(),Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        // 判断用户是否是业主身份
        ParkLotData itemData = parklotBItemMapper.findAdminUserIsExistByParklot(request);
        if(itemData!=null){
            // 用户类型 (0：车主 1：业主 2：管理员)
            Integer  adminType = itemData.getAdminType();
            if(adminType == Constants.PARKING_RENTER){
                // 该账户已经是业主身份，不可修改成管理员身份
                throw new ExcelException(Status.BItemErr.ACCOUNT_TYPE_CAROWNER.getCode(),Status.BItemErr.ACCOUNT_TYPE_CAROWNER.getMsg());
            }else if(adminType == Constants.PARKING_RENTEE){
                List<ParkLotData> isAdminTypeList = parklotBItemMapper.findAdminUserByApply(request);
                if(isAdminTypeList!=null&&isAdminTypeList.size()>0) {
                    // 该账户存在可开票余额，不可修改成管理员身份
                    throw new ExcelException(Status.BItemErr.ACCOUNT_TYPE_CAROWNER_RECEIPT_MONEY.getCode(), Status.BItemErr.ACCOUNT_TYPE_CAROWNER_RECEIPT_MONEY.getMsg());
                }


                List<ParkLotData> isExistOrderList = parklotBItemMapper.findAdminByOrder(request);
                if(isExistOrderList!=null&&isExistOrderList.size()>0) {
                    // 该车主存在进行中订单，不可修改成管理员身份
                    throw new ExcelException(Status.BItemErr.ACCOUNT_TYPE_CAROWNER_EXISTS_ORDER.getCode(), Status.BItemErr.ACCOUNT_TYPE_CAROWNER_EXISTS_ORDER.getMsg());
                }
            }else if(adminType == Constants.PARKING_ADMIN){
                // 判断用户是否是其他小区的管理员
                Integer parklotId = itemData.getId();
                if(parklotId!=null && parklotId!=request.getParkId()){
                    // 该账户已经是其它小区管理员
                    throw new ExcelException(Status.BItemErr.ACCOUNT_TYPE_CAROWNER_EXISTS.getCode(),Status.BItemErr.ACCOUNT_TYPE_CAROWNER_EXISTS.getMsg());
                }
            }
        }
        Integer flat = parklotBItemMapper.updateAdminUserDetailed(request);
        return RespUtil.successResp(flat);
    }

}
