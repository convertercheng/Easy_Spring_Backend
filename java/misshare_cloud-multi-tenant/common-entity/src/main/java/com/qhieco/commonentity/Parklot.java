package com.qhieco.commonentity;

import com.qhieco.TenantSupport;
import com.qhieco.constant.Status;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/8 17:46
 * <p>
 * 类说明：
 * 对应停车区表
 */
@Data
@Entity
@Table(name = "t_parklot")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "integer")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Parklot implements TenantSupport{

    public static final Integer PARKLOT_TYPE_LIMIT = 3;

    @Id
    @Column(name = "id")
    @GeneratedValue
    private Integer id;

    /**
     * 停车区名称
     */
    @Column(name = "name")
    private String name;

    /**
     * 停车区别名
     */
    @Column(name = "alias")
    private String alias;

    /**
     * 停车区描述
     */
    @Column(name = "intro")
    private String intro;

    /**
     * 收费标准说明
     */
    @Column(name = "fee_intro")
    private String feeIntro;

    /**
     * 停车区地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 停车区经度
     */
    @Column(name = "lng")
    private Double lng;

    /**
     * 停车区纬度
     */
    @Column(name = "lat")
    private Double lat;

    /**
     * 停车区入口经度
     */
    @Column(name = "navi_lng")
    private Double naviLng;

    /**
     * 停车区入口纬度
     */
    @Column(name = "navi_lat")
    private Double naviLat;

    /**
     * 停车区所在地域id
     */
    @Column(name = "area_id")
    private Integer areaId;

    /**
     * 停车区运营商id
     */
    @Column(name = "operator_id")
    private Integer operatorId;
    /**
     * 移动用户id
     */
    @Column(name = "mobile_user_id")
    private Integer mobileUserId;

    /**
     * 停车区等级id
     */
    @Column(name = "parklot_rank_id")
    private Integer parklotRankId;

    /**
     * 紧急联系人姓名
     */
    @Column(name = "contact_name")
    private String contactName;

    /**
     * 紧急联系人号码
     */
    @Column(name = "contact_phone")
    private String contactPhone;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Long createTime;

    /**
     * 编辑时间
     */
    @Column(name = "modify_time")
    private Long modifyTime;

    /**
     * 停车区类型，0是静态车场，1是约车场，2是约车位
     */
    @Column(name = "type")
    private Integer type;

    /**
     * 类型，0:室内，1:室外，2:室内+室外
     */
    @Column(name = "kind")
    private Integer kind;

    /**
     * 是否安装继电器，0：没安装 1：安装
     */
    @Column(name = "has_relay")
    private Integer hasRelay;

    /**
     * 状态
     */
    @Column(name = "state")
    private Integer state;


    /**
     * 道闸厂商id，0：博思高，1：科拓
     */
    @Column(name = "barrier_manufacturer")
    private Integer barrierManufacturer;

    /**
     * 第三方的停车区id
     */
    @Column(name = "extra_parklot_id")
    private String extraParklotId;

    /**
     * 是否收取停车费
     */
    @Column(name = "allocable")
    private Integer allocable;

    /**
     * 0是普通，1是内部共享
     */
    @Column(name = "innershare", nullable = false)
    private Integer innershare;

    /**
     * 计费类型（0是道闸计费，1是地锁计费）
     */
    @Column(name = "charge_type")
    private Integer chargeType;

    @Column
    private Integer tenantId;

    /**
     * 与当前定位位置的距离
     */
    @Transient
    private Integer locateDistance;

    /**
     * 总车位数量
     */
    @Transient
    private Integer totalAmount;

    /**
     * 签约车位数量
     */
    @Transient
    private Integer signedAmount;

    @Transient
    private String areaName;

    @Transient
    private String provinceName;

    @Transient
    private String cityName;

    @Transient
    private String typeStr;

    @Transient
    private String innershareStr;

    @Transient
    private String tenantName;

    public String getTypeStr(){
        return Status.ParklotType.find(this.getType());
    }
    public String getInnershareStr(){
        return Status.ParkingInner.find(this.getInnershare());
    }

}