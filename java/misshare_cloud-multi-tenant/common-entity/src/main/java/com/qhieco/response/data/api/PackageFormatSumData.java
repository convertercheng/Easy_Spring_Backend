package com.qhieco.response.data.api;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PackageFormatSumData {
    private Integer daytime;
    private BigDecimal sumNumber=new BigDecimal(0);
    private Integer id;
}
