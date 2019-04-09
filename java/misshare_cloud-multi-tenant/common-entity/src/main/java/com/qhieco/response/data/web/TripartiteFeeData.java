package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

@Data
public class TripartiteFeeData{
    private List<TripartiteFee> tripartiteFeeList;

    public TripartiteFeeData() {
    }

    public TripartiteFeeData(List<TripartiteFee> tripartiteFeeList) {
        this.tripartiteFeeList = tripartiteFeeList;
    }

    @Data
    public class TripartiteFee {
        private String tripartiteFee;
        private String zfbFee;
        private String wxFee;
        private Long time;

        public TripartiteFee(String tripartiteFee, String zfbFee, String wxFee, Long time) {
            this.tripartiteFee = tripartiteFee;
            this.zfbFee = zfbFee;
            this.wxFee = wxFee;
            this.time = time;
        }

    }
}
