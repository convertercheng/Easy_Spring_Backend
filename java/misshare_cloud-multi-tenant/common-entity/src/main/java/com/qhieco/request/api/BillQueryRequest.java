package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/2 13:53
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class BillQueryRequest extends AbstractRequest {

    private Integer user_id;

    private Integer page_num;
    /**
     * 类型
     * -1: 全部
     * 0: 预约消费。车主支出的预约车位
     * 1: 停车消费。车主支出的停车费
     * 2: 退款。退还到账户的金额
     * 4: 提现。业主、物业提取的金额
     * 6: 预约收费。业主、物业的预约费收入
     * 7: 停车收费。业主、物业的停车费收入
     */
    private String type;
    /**
     * 账单日期，格式:yyyyMM
     */
    private String date;

    public static final String TYPE_ALL = "-1";
    public static final String TYPE_RESERVE_CONSUME = "0";
    public static final String TYPE_PARKING_CONSUME = "1";
    public static final String TYPE_REFUND = "2";
    public static final String TYPE_WITHDRAW = "4";
    public static final String TYPE_RESERVE_CHARGE = "6";
    public static final String TYPE_PARKING_CHARGE = "7";
}
