package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/31 9:55
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class UserData {

    private Integer id;

    private String path;

    private String phone;

    private Long latestLoginTime;

    private Long registerTime;

    private Integer state;

    private Integer userType;

    private BigDecimal balanceEarn;

    private List<String> plateNumber;

    private String stateStr;

    private String userTypeStr;

    private String name;

    private String identityNumber;

    private String source;
    private String medium;
    private String content;
    private String keyword;
    private String series;
    private String plateNumberStr;
    private Integer orderCount;
    private Long orderCreateTime;
    private Integer parklocCount;
    private String wxUnionId;
    private Integer integral;

    private String username; // 账户名
    private String companyName; // 公司名称
    private String liaisons; // 联系人
    private String liaisonsPhone; // 联系电话
    private Long createTime; // 创建时间

    private String roleName; // 角色名称
    private Integer roleId;  // 所属角色ID

    private Integer userPlateId;// 用户套餐ID

    private List<ParkLotData> parklotData; // 账户关联小区


    public String getStateStr() {
        return Status.Common.find(this.state);
    }

}
