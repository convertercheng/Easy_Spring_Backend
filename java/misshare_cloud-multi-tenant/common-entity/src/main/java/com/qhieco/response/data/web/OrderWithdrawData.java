package com.qhieco.response.data.web;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 下午4:52
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderWithdrawData {
    private Integer id;

    private Integer fileId;

    private BigDecimal balance;

    private Long applyTime;

    private Long completeTime;

    private Integer state;

    private String accountInfo;

    private String phone;

    private String message;

    private String userWebName;

    private BigDecimal balanceEarn;

    private String modePayment;

    private String filePath;

    private String userLevelId;

    private String stateStr;
}
