package com.qhieco.request.api;

import com.qhieco.constant.Constants;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/2/23 16:48
 * <p>
 * 类说明：
 * 广告页方法请求参数
 */
@Data
public class ParkingRuleData {

    private Long timePointer = null;
    private BigDecimal parkingFee = Constants.BIGDECIMAL_ZERO;
    private Integer freeUseTime = null;
    private BigDecimal firstHourFee = null;

}
