package com.qhieco.webbitem.controller;

import com.qhieco.bitemservice.ParklotBItemService;
import com.qhieco.bitemservice.exception.ParamException;
import com.qhieco.constant.Status;
import com.qhieco.request.web.ParklotRequest;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/3 14:00
 * <p>
 * 类说明：
 */
@RestController
@RequestMapping("/parklot")
@Slf4j
public class ParklotBItemWeb {

    @Autowired
    ParklotBItemService parklotBItemService;

    /**
     * 查询可用的所有小区列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/usablelist")
    public Resp findAllUsableParklot(ParklotRequest request) {
        return parklotBItemService.findAllUsableParklot(request);
    }

    /**
     * 获取分配小区的所有列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "/userlist")
    public Resp findByUserAllParklot(ParklotRequest request) {
        return parklotBItemService.findByUserAllParklot(request);
    }

    /**
     * 分页获取小区和停车场管理员信息
     * @param request
     * @return
     */
    @PostMapping(value = "/findParklotAndAdmin")
    public Resp findParklotAndAdmin(ParklotRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return parklotBItemService.findParklotAdminByPage(request);
    }

    /**
     * 根据小区获取小区停车场管理员详细
     * @param request
     * @return
     */
    @PostMapping(value = "/findAdminUserDetailed")
    public Resp findAdminUserDetailed(ParklotRequest request) {
        Resp checkResp = ParamCheck.check(request, "adminId");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return parklotBItemService.findAdminUserDetailed(request);
    }

    /**
     * 更新停车场管理员信息
     * @param request
     * @return
     */
    @PostMapping(value = "/updateAdminUserDetailed")
    public Resp updateAdminUserDetailed(ParklotRequest request) {
        // 小区ID，管理员ID，管理员名称，电话号码
        Resp checkResp = ParamCheck.check(request, "parkId","adminId","adminName","adminPhone");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.BItemErr.SYSTEM_ERROR.getCode());
        }
        return parklotBItemService.updateAdminUserDetailed(request);
    }

}
