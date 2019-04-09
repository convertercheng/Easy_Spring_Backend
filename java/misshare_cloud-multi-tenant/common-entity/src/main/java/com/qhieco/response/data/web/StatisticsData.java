package com.qhieco.response.data.web;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/6/12 9:25
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class StatisticsData {

    /**
     * 基本信息
     */
    private Integer id;
    private Integer activityId;
    private Integer userId;
    private Integer type;
    private Integer value;
    private Long createTime;


    private Integer countNum = 0;    // 人数统计
    private Integer sumNum = 0;      // 总数统计
    private Integer allSumNum = 0;   // 总数统计
    private Double avgNum = 0.0;     // 平均数统计
    private Integer allTypeSumNum=0;   // 所有活动统计
    private String hoursDate;
    private String daysDate;
    private Integer hours;
    private Integer days;
    private Integer weeks;
    private String createDate;

    private Integer checkNum=0;       // 浏览统计
    private Integer inviteNum=0;      // 邀请统计
    private Integer bindNum=0;        // 绑定车牌号统计
    private Integer firstOrderNum=0;  // 首次下单统计
    private Integer registerNum=0;    //注册人数统计


    private String acTime;      //活动时间
    private String acName;      //活动名称
    private Integer acType;     //活动类型
    private Integer acState;    //活动状态
    private Integer arType;     //规则计算类型
    private Integer arState;    //规则状态
    private Integer laType = 0; //阶梯类型（邀请，被邀请）
    private String laTypeStr;     //阶梯类型（邀请，被邀请）
    private String triggerTypes;//奖励触发途径
    private String triggerTypesStr; //奖励触发途径

    private String phone;           //获奖用户手机号码
    private String targetPhone;    //目标用户手机号码（邀请/被邀请）
    private String target1Phone;    //目标用户手机号码1（邀请/被邀请）
    private String target2Phone;    //目标用户手机号码2（邀请/被邀请）
    private String prizeName;       //奖品名称
    private Integer prizeNumber;    //奖品数量
    private Long prizeCreateTime;   //领取时间

    private String tagNames;//用户标签组


    public StatisticsData() {

    }

    public StatisticsData(Integer hours, Integer days, Integer weeks, String daysDate) {
        this.hours = hours;
        this.days = days;
        this.weeks = weeks;
        this.daysDate = daysDate;
    }

    public String getLaTypeStr() {
        return Status.LaType.find(this.laType);
    }

    public String getTriggerTypesStr() {
        String str = "";
        if (!StringUtils.isEmpty(this.triggerTypes)) {
            String[] arr = this.triggerTypes.split(Constants.DELIMITER_COMMA);
            List list = Arrays.asList(arr);
            Set set = new HashSet(list);
            arr = (String [])set.toArray(new String[0]);
            for (String s : arr) {
                try {
                    str += Status.TriggerType.find(Integer.valueOf(s)) + ";";
                } catch (Exception e) {
                }
            }
        }
        return str;
    }
}
