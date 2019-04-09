package com.qhieco.barrier.epaitc.request;

import lombok.Data;

import java.math.BigDecimal;


/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/29 下午3:32
 * <p>
 * 类说明：
 *     一拍停车道闸对接请求
 */
@Data
public class EnterRequest {


    /**
     * park_id : 123
     * tag : leave
     * inout_id : 12345
     * license : 粤ESV123
     * access_type : 1
     * enter_time : 2018-03-08T17:56:26+08:00
     * enter_portal : A1
     * enter_snapshot :
     * enter_operator :
     * leave_time : 2018-03-08T18:56:26+08:00
     * leave_portal : A2
     * leave_snapshot :
     * leave_operator :
     * total_amount : 10
     * discount_amount : 0
     * e_paid_amount : 5
     * cash_paid_amount : 5
     * result : 1
     */

    private Integer park_id;
    private String tag;
    private Integer inout_id;
    private String license;
    private Integer access_type;
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
    private Integer result;

    @Override
    public String toString() {
        return "EnterRequest{" +
                "park_id=" + park_id +
                ", tag='" + tag + '\'' +
                ", inout_id=" + inout_id +
                ", license='" + license + '\'' +
                ", access_type=" + access_type +
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
