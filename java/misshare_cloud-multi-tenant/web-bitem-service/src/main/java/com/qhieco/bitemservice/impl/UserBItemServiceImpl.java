package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.UserBItemService;
import com.qhieco.commonentity.UserBItem;
import com.qhieco.commonrepo.UserBItemWebRespository;
import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.ParkLotData;
import com.qhieco.response.data.web.UserData;
import com.qhieco.util.RespUtil;
import com.qhieco.webbitemmapper.ParklotBItemMapper;
import com.qhieco.webbitemmapper.PermissionBItemMapper;
import com.qhieco.webbitemmapper.UserBItemWebMapper;
import com.qhieco.webservice.exception.ExcelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 19:52
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class UserBItemServiceImpl implements UserBItemService {

    @Autowired
    UserBItemWebMapper userBItemWebMapper;

    @Autowired
    UserBItemWebRespository userBItemWebDao;

    @Autowired
    ParklotBItemMapper parklotBItemMapper;

    @Autowired
    PermissionBItemMapper permissionBItemMapper;

    /**
     * 更新登录密码
     * @param request
     * @return
     */
    @Override
    public Resp updateLoginPass(UserRequest request) {
        // 获取登录用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer userId = userBItemWebMapper.getUserByUserName(username);
        if (userId != null && userId != 0) {
            request.setId(userId);
        } else {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        String newPassword = request.getNewPass();
        String checkPassword = request.getCheckPass();
        if(!newPassword.equals(checkPassword)){
            throw new ExcelException(Status.BItemErr.PARAMS_USERPASS_CHECK_ERROR.getCode(), Status.BItemErr.PARAMS_USERPASS_CHECK_ERROR.getMsg());
        }

        Integer flat = userBItemWebMapper.updateLoginPass(request);

        if(flat==null || flat==0){
            throw new ExcelException(Status.BItemErr.INSERT_ERROR.getCode(), Status.BItemErr.INSERT_ERROR.getMsg());
        }
        return RespUtil.successResp(flat);
    }

    /**
     * 分页查询账户信息列表
     * @param request
     * @return
     */
    @Override
    public Resp<AbstractPaged<UserData>> pageUser(UserRequest request) {
        if (request.getState() == null) {
            request.setState(Status.Common.VALID.getInt());
        }

        // 获取登录用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer pId = userBItemWebMapper.getUserByUserName(username);
        if (pId != null && pId != 0) {
            request.setPid(pId);
        } else {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        List<UserData> userList = userBItemWebMapper.pageUser(request);
        Integer count = userBItemWebMapper.pageUserTotalCount(request);

        AbstractPaged<UserData> data = AbstractPaged.<UserData>builder().sEcho
                (request.getSEcho() + 1).iTotalRecords(count).
                iTotalDisplayRecords(count).dataList(userList).build();
        return RespUtil.successResp(data);
    }

    /**
     * 新增/编辑 企业账户(管理员权限操作)
     * @param request
     * @return
     */
    @Override
    public Resp<UserData> saveOrUpdate(UserRequest request) {
        Integer userId = request.getUserId();
        UserBItem user = new UserBItem();

        // 获取登录用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer pId = userBItemWebMapper.getUserByUserName(username);
        if (pId != null && pId != 0) {
            user.setPid(pId);
        } else {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        user.setState(request.getState());
        user.setCompanyName(request.getCompanyName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setCreateTime(System.currentTimeMillis());
        if (!StringUtils.isEmpty(request.getLiaisons())) {
            user.setLiaisons(request.getLiaisons());
        }
        if (!StringUtils.isEmpty(request.getLiaisonsPhone())) {
            user.setLiaisonsPhone(request.getLiaisonsPhone());
        }
        user.setId(userId);
        try {
            user = userBItemWebDao.save(user);
        } catch (Exception e) {
            throw new ExcelException(Status.BItemErr.INSERT_ERROR.getCode(), Status.BItemErr.INSERT_ERROR.getMsg());
        }
        if (user.getId() == null) {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        // SUPADMIN_ROLE
        // ENTERPRISE_ROLE
        // CHILD_ROLE
        initialiseRoleData(userId, user.getId(), request.getParklots(), Constants.ENTERPRISE_ROLE);
        return RespUtil.successResp();
    }

    /**
     * 新增/编辑 企业账户(企业操作)
     * @param request
     * @return
     */
    @Override
    public Resp<UserData> saveOrUpdateBySubaccount(UserRequest request) {
        Integer userId = request.getUserId();
        UserBItem user = new UserBItem();

        // 获取登录用户的ID
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        Integer pId = userBItemWebMapper.getUserByUserName(username);
        if (pId != null && pId != 0) {
            user.setPid(pId);
        } else {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        user.setState(request.getState());
        user.setCompanyName(request.getCompanyName());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setCreateTime(System.currentTimeMillis());
        if (!StringUtils.isEmpty(request.getLiaisons())) {
            user.setLiaisons(request.getLiaisons());
        }
        if (!StringUtils.isEmpty(request.getLiaisonsPhone())) {
            user.setLiaisonsPhone(request.getLiaisonsPhone());
        }
        user.setId(userId);
        try {
            user = userBItemWebDao.save(user);
        } catch (Exception e) {
            throw new ExcelException(Status.BItemErr.INSERT_ERROR.getCode(), Status.BItemErr.INSERT_ERROR.getMsg());
        }
        if (user.getId() == null) {
            throw new ExcelException(Status.BItemErr.SYSTEM_ERROR.getCode(), Status.BItemErr.SYSTEM_ERROR.getMsg());
        }

        // SUPADMIN_ROLE
        // ENTERPRISE_ROLE
        // CHILD_ROLE
        initialiseRoleData(userId, user.getId(), request.getParklots(),Constants.CHILD_ROLE);
        return RespUtil.successResp();
    }

    /**
     * 初始化账户与角色资源权限关联
     * @param itemId
     * @param userId
     * @param parklots
     * @param roleCode
     */
    public void initialiseRoleData(Integer itemId, Integer userId, String parklots, String roleCode) {
        if (itemId == null) {
            UserData userRoleData = userBItemWebMapper.findRoleByRoleName(roleCode);
            if (userRoleData != null && userRoleData.getRoleId() != null) {
                Integer roleId = userRoleData.getRoleId();
                // 初始化角色关联
                userBItemWebMapper.updateRoleByUser(roleId, userId);
                //List <PermissionBItemData> permiList = permissionBItemMapper.findByRoleId(roleId);
                // 初始化资源权限关联
                //userBItemWebMapper.updateRolePermissionByUser();
            }
        }

        // 更新小区所属用户
        if (parklots != null && !"".equals(parklots)) {
            String[] itemArray = parklots.split(",");
            if (itemArray.length > 0) {
                parklotBItemMapper.delRelationByUserId(userId);
                for (int i = 0; i < itemArray.length; i++) {
                    ParklotRequest paramData = new ParklotRequest();
                    paramData.setUserId(userId);
                    paramData.setId(itemArray[i]);
                    parklotBItemMapper.insert(paramData);
                }
            }
        }
    }

    /**
     * 获取账户详情
     * @param id
     * @return
     */
    @Override
    public Resp<UserData> findUserDetailByUserId(Integer id) {
        UserData userData = userBItemWebMapper.findOne(id);
        ParklotRequest parklotRequest = new ParklotRequest();
        parklotRequest.setUserId(id);
        List<ParkLotData> parkLotData = parklotBItemMapper.findByUserAllParklot(parklotRequest);
        userData.setParklotData(parkLotData);
        return RespUtil.successResp(userData);
    }
}