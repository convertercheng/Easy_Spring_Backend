package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

@Data
public class WithdrawFeeData {
    private List<WithdrawFee> withdrawFeeList;

    public WithdrawFeeData() {
    }

    public WithdrawFeeData(List<WithdrawFee> withdrawFeeList) {
        this.withdrawFeeList = withdrawFeeList;
    }


    @Data
    public class WithdrawFee {
        private String userFee;
        private String ownerFee;
        private String estateFee;
        private Long time;

        public WithdrawFee(String userFee, String ownerFee, String estateFee, Long time) {
            this.userFee = userFee;
            this.ownerFee = ownerFee;
            this.estateFee = estateFee;
            this.time = time;
        }

    }
}
