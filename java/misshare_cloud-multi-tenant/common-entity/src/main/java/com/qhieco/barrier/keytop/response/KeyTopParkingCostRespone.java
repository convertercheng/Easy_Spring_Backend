package com.qhieco.barrier.keytop.response;

import com.qhieco.commonentity.Parklot;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 9:28
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class KeyTopParkingCostRespone {
    private String Status;
    private String Message;
    private ParklotOrderCost Data;
    private String InOrOutFlag;

    @Data
    public static class  ParklotOrderCost{
        /**
         * 车牌号码
         */
        private String PlateNumber;
        /**
         * 卡片编号
         */
        private String CardNo;
        /**
         * 车辆所在车场ID
         */
        private String ParkingID;
        /**
         * 车辆所在车场描述
         */
        private String ParkName;
        /**
         * 车辆进场时间(yyyyMMddHHmmss)
         */
        private String EntranceDate;

        /**
         * 订单信息
         */
        private Pkorder Pkorder;
        /**
         * 剩余出场时间(分钟)
         */
        private String OutTime;
        /**
         * 状态码 枚举值
         */
        private String Result;
        /**
         * 最后缴费时间(yyyyMMddHHmmss)
         */
        private String PayDate;

        @Data
        public static class Pkorder{
            /**
             * 纪录 ID
             */
            private String RecordID;
            /**
             * 订单 ID
             */
            private String OrderID;
            /**
             * 订单类型（1.临时卡缴费、2 月卡续费）
             */
            private Integer OrderType;
            /**
             * 支付方式
             */
            private Integer PayWay;
            /**
             * 订单金额单位分
             */
            private BigDecimal Amount;
            /**
             * 未支付金额
             */
            private BigDecimal OutstandingAmount;
            /**
             * 已付费用
             */
            private BigDecimal PayAmount;
            /**
             * 订单状态（-1 作废、0 未生效、1 生效）
             */
            private String Status;
            /**
             * 订单时间(yyyyMMddHHmmss)
             */
            private String OrderTime;
            /**
             * 原有效期(yyyyMMddHHmmss)
             */
            private String OldUserulDate;
            /**
             * 月卡现有效期(yyyyMMddHHmmss)
             */
            private String NewUsefulDate;
            /**
             * 线上账户 ID
             */
            private String OnlineUserID;
            /**
             * 线上订单编号
             */
            private String OnlineOrderID;
        }
    }
}
