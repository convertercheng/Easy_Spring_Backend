package com.qhieco.web.controller;

import com.qhieco.commonentity.Activity;
import com.qhieco.commonentity.ActivityRule;
import com.qhieco.commonentity.relational.ActivityTagB;
import com.qhieco.constant.Status;
import com.qhieco.request.web.*;
import com.qhieco.response.Resp;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.ActivityService;
import com.qhieco.webservice.exception.ParamException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 19:28
 * <p>
 * 类说明：
 * ${说明}
 */
@RestController
@RequestMapping(value = "activity")
@Slf4j
public class ActivityWeb {

    @Autowired
    private ActivityService activityService;

    /**
     * 查询活动列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "list")
    public Resp queryActivityList(ActivityQuery request) {
        log.info("查询活动列表参数， " + request);
        return RespUtil.successResp(activityService.queryActivityList(request));
    }

    /**
     * 通过活动id查询活动详情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "detail")
    public Resp activityDetail(@Param("id") Integer id) {
        return RespUtil.successResp(activityService.queryActivityDetailInfo(id));
    }

    /**
     * 保存和更新活动数据
     *
     * @param request
     * @return
     */
    @PostMapping("/saveOrUpdate")
    public Resp saveOrUpdate(ActivitySaveRequest request) {
        Integer id = request.getId();
        String name = request.getName();
        String intro = request.getIntro();
        Long beginTime = request.getBeginTime();
        Long endTime = request.getEndTime();
        Integer type = request.getType();
        String href = request.getHref();
        MultipartFile fileLong = request.getFileLong();
        MultipartFile fileWide = request.getFileWide();
        Integer fileLongId = request.getFileLongId();
        Integer fileWideId = request.getFileWideId();
        List<ActivityRule> activityRules = request.getActivityRules();
        List<ActivityTagB> activityTagBs = request.getActivityTagBs();


        log.info("id=" + id + ", name=" + name + ", intro=" + intro + ", beginTime=" + beginTime + ", endTime=" + endTime
                + ", type= " + type + ",  href=" + href + ", fileLongId=" + fileLongId + ", fileWideId=" + fileWideId);
        if (StringUtils.isEmpty(name) || type == null || beginTime == null || endTime == null || beginTime >= endTime) {
            log.error(" 活动数据不完整，保存失败");
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }

        if (fileLong != null && fileLong.getSize() > 2000000) {
            log.error("文件大小超过2M，不能上传，" + fileLong.getSize());
            return RespUtil.errorResp(Status.WebErr.FILE_TOOBIG_ERROR.getCode(), Status.WebErr.FILE_TOOBIG_ERROR.getMsg());
        } else if (fileWide != null && fileWide.getSize() > 2000000) {
            log.error("文件大小超过2M，不能上传，" + fileWide.getSize());
            return RespUtil.errorResp(Status.WebErr.FILE_TOOBIG_ERROR.getCode(), Status.WebErr.FILE_TOOBIG_ERROR.getMsg());
        }

        Long now = System.currentTimeMillis();
        Activity activity = new Activity(name, intro, beginTime, endTime, type, href, now, Status.Common.VALID.getInt());
        if (!StringUtils.isEmpty(id)) {
            activity.setId(Integer.valueOf(id));
        }
        return activityService.saveOrUpdate(activity, fileLong, fileWide, fileLongId, fileWideId, activityRules, activityTagBs, now);
    }

    @GetMapping(value = "delete")
    public Resp delete(@Param("id") Integer id) {
        if (id == null) {
            log.error(" 参数不正确，删除失败");
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        activityService.deleteActivity(id);
        return RespUtil.successResp();
    }

    /**
     * 保存运营活动奖品
     *
     * @param entityRequest
     * @return
     */
    @PostMapping(value = "prize/saveOrUpdate")
    public Resp saveOrUpdatePrize(PrizeEntityRequest entityRequest) {
        try {
            log.info("保存运营活动奖品参数 ， entityRequest = " + entityRequest);
            Resp checkResp = ParamCheck.check(entityRequest, "name", "type", "money");
            if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
                throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
            }

            if (StringUtils.isEmpty(entityRequest.getId()) && (entityRequest.getFiles() == null || entityRequest.getFiles().size() == 0)) {
                log.error("首次保存必须上传图片");
                return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
            }
            int countRepeat = activityService.countRepeatPrizeName(entityRequest.getId(), entityRequest.getName());
            if (countRepeat > 0) {
                return RespUtil.errorResp(Status.WebErr.PRIZE_NAME_REPEAT.getCode(), Status.WebErr.PRIZE_NAME_REPEAT.getMsg());
            }

            activityService.saveOrUpdatePrize(entityRequest);
            return RespUtil.successResp();
        } catch (Exception e) {
            log.error("保存运营活动奖品异常，" + e);
        }
        return RespUtil.errorResp(Status.ApiErr.INSERT_ERROR.getCode(), Status.ApiErr.INSERT_ERROR.getMsg());
    }

    /**
     * 查询奖品详情
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/detail")
    public Resp queryPrizeDetail(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return RespUtil.successResp(activityService.queryPrizeDetailById(id));
    }

    /**
     * 奖品管理列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "prize/list")
    public Resp queryList(PrizeListRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }

        return RespUtil.successResp(activityService.queryPrizeList(request.getName(), request.getStart(), request.getLength(), request.getSEcho()));
    }

    /**
     * 奖品领取记录列表
     *
     * @return
     */
    @PostMapping(value = "prize/receive/list")
    public Resp queryPrizeReceiveList(PrizeReceiveListRequest request) {
        Resp checkResp = ParamCheck.check(request, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return RespUtil.successResp(activityService.queryPrizeReceiveList(request));
    }

    /**
     * 导出奖品领取记录列表
     *
     * @return
     */
    @PostMapping(value = "prize/receive/list/excel")
    public void queryPrizeReceiveListExcel(PrizeReceiveListRequest request, HttpServletResponse response) {
        // 下载文件的默认名称
        try {
            activityService.queryPrizeReceiveListExcel(request, response.getOutputStream());
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(("奖品领取记录" + ".xls").getBytes(), "iso-8859-1"));
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
        } catch (Exception e) {
            log.error("导出奖品领取列表异常 ， " + e.getMessage());
            response.setStatus(404);
        }
    }

    /**
     * 设置活动规则的选择奖品选项接口
     *
     * @return
     */
    @GetMapping(value = "prize/all")
    public Resp queryValidPrizeList(@Param("startTime")Long startTime, @Param("endTime") Long endTime) {
        if (endTime == null || startTime == null) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return RespUtil.successResp(activityService.queryValidPrizeAll(startTime, endTime));
    }

    /**
     * 删除奖品
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/delete")
    public Resp deletePrize(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.deletePrize(id);
    }

    /**
     * 判断奖品是否可以删除
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/delete/check")
    public Resp checkDelCondition(Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.checkDeletePrizeCondition(id);
    }

    /**
     * 冻结奖品
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/frozen")
    public Resp frozenPrize(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.frozenPrize(id);
    }

    /**
     * 判断是否可以冻结奖品
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/frozen/check")
    public Resp checkFrozenPrize(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.checkFrozenPrize(id);
    }

    /**
     * 解冻奖品
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/unfreeze")
    public Resp unfreezePrize(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.unfreezePrize(id);
    }

    /**
     * 判断是否可以解冻奖品
     *
     * @param id
     * @return
     */
    @PostMapping(value = "prize/unfreeze/check")
    public Resp checkUnfreezePrize(@Param("id") Integer id) {
        if (StringUtils.isEmpty(id)) {
            return RespUtil.errorResp(Status.WebErr.PARAM_ERROR.getCode(), Status.WebErr.PARAM_ERROR.getMsg());
        }
        return activityService.checkUnfreezePrize(id);
    }

    /**
     * 查询"邀请"活动参与用户列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "invite/list")
    public Resp queryInviteList(ActivityQuery request) {
        log.info("查询邀请活动关联信息列表");
        return RespUtil.successResp(activityService.queryActivityByInviteList(request));
    }

    /**
     * 查询"注册"活动参与用户列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "register/list")
    public Resp queryRegisterList(ActivityQuery request) {
        log.info("查询注册活动关联信息列表");
        return RespUtil.successResp(activityService.queryActivityByRegisterList(request));
    }

    /**
     * 查询"绑定车牌"活动参与详情列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "bindCarPlate/list")
    public Resp queryBindCarPlateList(ActivityQuery request) {
        log.info("查询绑定车牌活动关联信息列表");
        return RespUtil.successResp(activityService.queryActivityByBindCarPlateList(request));
    }

    /**
     * 查询"首次"下单活动参与详情列表
     *
     * @param request
     * @return
     */
    @PostMapping(value = "firstOrer/list")
    public Resp queryFirstOrerList(ActivityQuery request) {
        log.info("查询首次下单活动关联信息列表");
        return RespUtil.successResp(activityService.queryActivityByFirstOrerList(request));
    }
}
