package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/7/11 9:52
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class DiscountPackageRequest extends QueryPaged {

    private Integer id;
    private String name;       //名称
    private Integer type;       //类型(1:停车类)
    private Integer effectiveDaytime;  //套餐有效期(天),0为无期限
    private Long effectiveBeginTime;  //有效期开始时间
    private Long effectiveEndTime;    //有效期结束时间
    private Integer infoRule;   //生效规则，默认1 立即生效
    private Integer toplimit;   //开通上限
    private Integer realQuantity;      //当前开通数量
    private String packageAmount;//套餐金额
    private Integer state;      //状态(1已上架,2已下架,3待上架,4已满额)
    private String descript;    //描述
    private Long updateTime;   //更新时间
    private Long createTime;   //创建时间

    private Integer packageState;      //状态(0正常,1到期,2过期)
    private Integer inNumber;   // 剩余天数
    private String number;  // 车牌号
    private Integer parklotState;   // 小区关联套餐展示状态(1展示，0不展示)
    private Integer parklotId;   // 小区ID



    /**
     * 时段
     */
    private Integer ruleType;   //时段类型
    private String ruleTimeBegin; //开始时间
    private String ruleTimeEnd;   //结束时间
    private String[] everyDay;   //每天
    private String[] workDay;    //工作日
    private String[] weekDay;    //周末

    private Integer sumId; //套餐规格id



}
