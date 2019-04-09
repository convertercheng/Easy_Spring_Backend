package com.qhieco.request.web;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-30 下午5:49
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class OrderWithdrawRequest extends QueryPaged {
    private Integer id;
    private String phone;
    private BigDecimal balanceMin;
    private BigDecimal balanceMax;
    private String accountInfo;
    private Long startApplyTime;
    private Long endApplyTime;
    private Integer state;
    private Integer webUserId;
    private String filePath;
    private String message;

    @Override
    public String toString() {
        return "OrderWithdrawRequest{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", balanceMin=" + balanceMin +
                ", balanceMax=" + balanceMax +
                ", accountInfo='" + accountInfo + '\'' +
                ", startApplyTime=" + startApplyTime +
                ", endApplyTime=" + endApplyTime +
                ", state=" + state +
                ", webUserId=" + webUserId +
                ", message='" + message + '\'' +
                '}';
    }
}
