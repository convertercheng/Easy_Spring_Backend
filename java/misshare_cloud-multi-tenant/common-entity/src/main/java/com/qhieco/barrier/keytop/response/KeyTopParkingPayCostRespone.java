package com.qhieco.barrier.keytop.response;

import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 9:28
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class KeyTopParkingPayCostRespone {
    /**
     * 状态值
     */
    private String result;
    /**
     * 支付成功的订单对象
     */
    private ParklotOrderPayCost data;

    @Data
    public static class  ParklotOrderPayCost{
        /**
         * 状态码
         */
        private String Result;
        /**
         * 支付时间(yyyyMMddHHmmss)
         */
        private String PayDate;
        /**
         * 线上订单唯一编号
         */
        private String OnlineOrderID;
    }
}
