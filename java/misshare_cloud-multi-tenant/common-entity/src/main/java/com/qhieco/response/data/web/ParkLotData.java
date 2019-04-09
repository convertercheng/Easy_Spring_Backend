package com.qhieco.response.data.web;

import com.qhieco.commonentity.FeeRuleReserve;
import com.qhieco.commonentity.File;
import com.qhieco.commonentity.Parklot;
import com.qhieco.commonentity.UserMobile;
import com.qhieco.commonentity.iotdevice.Access;
import com.qhieco.commonentity.iotdevice.Barrier;
import com.qhieco.commonentity.iotdevice.Relaymeter;
import com.qhieco.commonentity.relational.ParklotParamsB;
import lombok.Data;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/4/2 9:50
 * <p>
 * 类说明：
 *     小区详情返回数据
 */
@Data
public class ParkLotData extends Parklot{
    private Integer cityId;
    private Integer provinceId;
    private Integer areaId;
    private Integer idleAmount;
    private List<File> fileList;
    private String parkLotValue;
    private Integer leftAmountType;
    private Integer leftAmount;
    private List<FeeRuleReserve> feeRuleReserveList;
    private ParkingFeeRuleInfoData parkingFeeRuleInfoData;
    private List<ParklotParamsB> parklotTimeParamsList;
    private List<ParklotParamsB> parklotPlatformParamsList;
    private UserMobile userMobile;
    private List<Barrier> barrierList;
    private List<Access> accessList;
    private List<Relaymeter> relaymeterList;
    private Integer innershare;
    private String innershareStr;
    private Integer chargeType;

    private Integer id;
    private String name;
    private String address;

    private Integer adminId; // 停车场管理员ID
    private String adminName; // 管理员名称
    private String adminPhone; // 管理员联系方式

    private Integer adminType; // 管理员用户身份

    private DiscountPackageData packageByParkLot; // 小区绑定套餐

}
