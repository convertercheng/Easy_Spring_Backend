package com.qhieco.response.data.api;

import java.math.BigDecimal;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/2 17:01
 * <p>
 * 类说明：
 * ${说明}
 */
@lombok.Data
public class BillDetailRepData {
    /**
     * 0:预约订单，1:停车订单，2:退款订单，3:充值订单，4:提现订单
     */
    private Integer type;

    /**
     * 账单时间
     */
    private Long billTime;

    /**
     * 订单金额
     */
    private BigDecimal fee;

    private Integer state;

    /**
     * 订单编号
     */
    private String serialNumber;

    /**
     * 支付流水号
     */
    private String tradeNo;
    /**
     *  支付方式。若是支出账单，则显示支付方式，包括：微信、支付宝。提现账单不显示此栏信息
     */
    private Integer channel;

    private String bankName;
    private String bankNumber;
}
