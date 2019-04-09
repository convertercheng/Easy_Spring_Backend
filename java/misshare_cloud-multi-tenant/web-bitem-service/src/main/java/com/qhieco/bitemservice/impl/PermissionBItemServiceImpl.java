package com.qhieco.bitemservice.impl;

import com.qhieco.bitemservice.PermissionBItemService;
import com.qhieco.bitemservice.UserBItemService;
import com.qhieco.commonentity.PermissionBItem;
import com.qhieco.constant.Status;
import com.qhieco.request.web.UserRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AbstractPaged;
import com.qhieco.response.data.web.PermissionBItemData;
import com.qhieco.response.data.web.UserData;
import com.qhieco.util.RespUtil;
import com.qhieco.webbitemmapper.PermissionBItemMapper;
import com.qhieco.webbitemmapper.UserBItemWebMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/27 19:52
 * <p>
 * 类说明：
 * ${description}
 */
@Service
public class PermissionBItemServiceImpl implements PermissionBItemService {

    @Autowired
    PermissionBItemMapper permissionBItemMapper;

    @Override
    public List<PermissionBItemData> findByUserId(Integer userId) {
        return permissionBItemMapper.findByUserId(userId);
    }
}
