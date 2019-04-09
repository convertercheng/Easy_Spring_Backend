package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;
import java.util.List;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/1 10:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DiscountPackageData {

    /**
     * 基本信息
     */
    private Integer id;
    private String name;       //名称
    private Integer type;       //类型(1:停车类)
    private Integer effectiveDaytime;  //套餐有效期(天),0为无期限
    private Long effectiveBeginTime;  //有效期开始时间
    private Long effectiveEndTime;    //有效期结束时间
    private Integer toplimit;   //开通上限
    private Integer infoRule;   //生效规则，默认1 立即生效
    private Integer realQuantity = 0;      //当前开通数量
    private Integer packageAmount;//套餐金额
    private Integer state;      //状态(1已上架,2已下架,3待上架,4已满额)
    private String descript;    //描述
    private Long updateTime;   //更新时间
    private Long createTime;   //创建时间

    /**
     * 时段
     */
    private List<DiscountRuleTimeData> ruleTimeList;    //时段
    private List<DiscountFormatSumData> formatSumList;   //规格
//    private Integer ruleState;     //时段状态
    private Integer ruleType;   //时段类型(每天，工作日/周末)

    private Integer packageId; //车牌套餐ID
    private String number; // 车牌号
    private Long realStartTime; // 开始时间
    private Long realEndTime; // 结束时间
    private Integer packageState; // 套餐状态 (1正常，2到期，3过期)
    private Integer inNumber = 0; // 剩余天数
    private String effectiveTime = "2099-12-31"; //有效期
    private String termState;//期限状态
    private Integer overNumber = 0;//剩余数量


    private String stateStr; //状态
    private String typeStr; //类型
//    private String ruleTimeTypeStr;   //时段类型
    private Integer packFlot; // 小区套餐关联是否展示(1展示，0不展示)

    public String getStateStr() {
        return Status.PackageState.find(this.state);
    }

    public String getTypeStr() {
        return Status.PackageType.find(this.type);
    }

    public String getPackFlotStr() {
        return Status.PackFlot.find(this.type);
    }
}
