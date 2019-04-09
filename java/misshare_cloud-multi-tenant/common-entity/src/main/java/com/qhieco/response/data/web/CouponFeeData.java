package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

@Data
public class CouponFeeData {
    private List<CouponFee> couponFeeList;

    public CouponFeeData() {
    }

    public CouponFeeData(List<CouponFee> couponFeeList) {
        this.couponFeeList = couponFeeList;
    }


    @Data
    public class CouponFee {
        private String couponUsedFee;
        private String couponLimitFee;
        private Long time;

        public CouponFee(String couponUsedFee,String couponLimitFee, Long time) {
            this.couponUsedFee = couponUsedFee;
            this.couponLimitFee = couponLimitFee;
            this.time = time;
        }

    }
}
