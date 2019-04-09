package com.qhieco.response.data.web;

import lombok.Data;

import java.util.List;

/**
 * Created by xujiayu on 17/11/12.
 */
@Data
public class InvoiceFeeData{
    private List<InvoiceFee> invoiceFeeList;

    public InvoiceFeeData() {
    }

    public InvoiceFeeData(List<InvoiceFee> invoiceFeeList) {
        this.invoiceFeeList = invoiceFeeList;
    }


    @Data
    public class InvoiceFee {
        private String invoiceFeeTotal;
        private String invoiceFee;
        private String invoiceFeeUnexecuted;
        private Long time;

        public InvoiceFee(String invoiceFeeTotal, String invoiceFee, String invoiceFeeUnexecuted, Long time) {
            this.invoiceFeeTotal = invoiceFeeTotal;
            this.invoiceFee = invoiceFee;
            this.invoiceFeeUnexecuted = invoiceFeeUnexecuted;
            this.time = time;
        }

    }
}
