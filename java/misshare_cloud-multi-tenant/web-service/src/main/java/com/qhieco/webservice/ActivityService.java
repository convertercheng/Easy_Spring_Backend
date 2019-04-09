package com.qhieco.webservice;

import com.qhieco.commonentity.Activity;
import com.qhieco.commonentity.ActivityRule;
import com.qhieco.commonentity.Prize;
import com.qhieco.commonentity.relational.ActivityTagB;
import com.qhieco.request.web.ActivityQuery;
import com.qhieco.request.web.PrizeEntityRequest;
import com.qhieco.request.web.PrizeReceiveListRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.OutputStream;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/13 19:29
 * <p>
 * 类说明：
 * ${说明}
 */
public interface ActivityService {

    public Resp saveOrUpdate(Activity activity, MultipartFile fileLong, MultipartFile fileWide, Integer fileLongId, Integer fileWideId, List<ActivityRule> activityRules, List<ActivityTagB> activityTagBs, Long now);

    public AbstractPaged<ActivityInfoData> queryActivityList(ActivityQuery request);

    public ActivityDetailData queryActivityDetailInfo(Integer id);

    public void deleteActivity(Integer id);

    /**
     * 查询"邀请"活动参与用户列表
     *
     * @param request
     * @return
     */
    public List<Activity> queryActivityByInviteList(ActivityQuery request);

    /**
     * 查询"注册"活动参与用户列表
     *
     * @param request
     * @return
     */
    public List<Activity> queryActivityByRegisterList(ActivityQuery request);

    /**
     * 查询"绑定车牌"活动参与详情列表
     *
     * @param request
     * @return
     */
    public List<Activity> queryActivityByBindCarPlateList(ActivityQuery request);

    /**
     * 查询"首次"下单活动参与详情列表
     *
     * @param request
     * @return
     */
    public List<Activity> queryActivityByFirstOrerList(ActivityQuery request);

    /**
     * 更新和保存奖品
     *
     * @param request
     * @return
     * @throws Exception
     */
    public Prize saveOrUpdatePrize(PrizeEntityRequest request) throws Exception;

    public int countRepeatPrizeName(Integer id, String name);

    /**
     * 查询奖品详情方法
     *
     * @param id
     * @return
     */
    public PrizeEntityDetailData queryPrizeDetailById(Integer id);

    /**
     * 查询奖品列表
     *
     * @param name
     * @param startPage
     * @param pageSize
     * @param sEcho
     * @return
     */
    public AbstractPaged<PrizeEntityDetailData> queryPrizeList(String name, Integer startPage, Integer pageSize, Integer sEcho);

    /**
     * 查询奖品领取记录列表
     *
     * @param request
     * @return
     */
    public AbstractPaged<PrizeReceiveRecordData> queryPrizeReceiveList(PrizeReceiveListRequest request);

    public Resp queryPrizeReceiveListExcel(PrizeReceiveListRequest request, OutputStream outputStream) throws Exception;

    /**
     * 设置活动规则的选择奖品选项列表
     *
     * @return
     */
    public List<PrizeIdAdNameInfoData> queryValidPrizeAll(Long startTime, Long endTime);

    public Resp deletePrize(Integer paramInteger);

    public Resp frozenPrize(Integer id);

    public Resp unfreezePrize(Integer id);

    public Resp checkDeletePrizeCondition(Integer id);

    public Resp checkFrozenPrize(Integer id);

    public Resp checkUnfreezePrize(Integer id);
}
