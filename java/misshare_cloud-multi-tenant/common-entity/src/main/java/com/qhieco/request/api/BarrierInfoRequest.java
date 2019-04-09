package com.qhieco.request.api;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/22 下午10:11
 * <p>
 * 类说明：
 *     道闸信息请求
 */
@Data
public class BarrierInfoRequest{


    /**
     * parklot_id : 12
     * tag : leave
     * unique_id : 12345
     * license : 粤ESV123
     * enter_time : 1524310141000
     * enter_portal : A1
     * enter_snapshot :
     * enter_operator :
     * leave_time : 1524310141000
     * leave_portal : A2
     * leave_snapshot :
     * leave_operator :
     * total_amount : 10
     * discount_amount : 0
     * e_paid_amount : 5
     * cash_paid_amount : 5
     * result : 1
     */

    private String parklot_id;
    private String tag;
    private String unique_id;
    private String license;
    private String enter_time;
    private String enter_portal;
    private String enter_snapshot;
    private String enter_operator;
    private String leave_time;
    private String leave_portal;
    private String leave_snapshot;
    private String leave_operator;
    private BigDecimal total_amount;
    private BigDecimal discount_amount;
    private BigDecimal e_paid_amount;
    private BigDecimal cash_paid_amount;
    private BigDecimal unpaid_amount;
    private int result;

    @Override
    public String toString() {
        return "BarrierInfoRequest{" +
                "parklot_id=" + parklot_id +
                ", tag='" + tag + '\'' +
                ", unique_id='" + unique_id + '\'' +
                ", license='" + license + '\'' +
                ", enter_time='" + enter_time + '\'' +
                ", enter_portal='" + enter_portal + '\'' +
                ", enter_snapshot='" + enter_snapshot + '\'' +
                ", enter_operator='" + enter_operator + '\'' +
                ", leave_time='" + leave_time + '\'' +
                ", leave_portal='" + leave_portal + '\'' +
                ", leave_snapshot='" + leave_snapshot + '\'' +
                ", leave_operator='" + leave_operator + '\'' +
                ", total_amount=" + total_amount +
                ", discount_amount=" + discount_amount +
                ", e_paid_amount=" + e_paid_amount +
                ", cash_paid_amount=" + cash_paid_amount +
                ", result=" + result +
                '}';
    }
}
