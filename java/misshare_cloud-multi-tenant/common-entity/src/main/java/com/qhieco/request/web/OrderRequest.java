package com.qhieco.request.web;

import lombok.Data;

import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/21 下午5:20
 *          <p>
 *          类说明：
 */
@Data
public class OrderRequest extends QueryPaged {
    private String phone;
    private String tradeNo;
    private Long startCreateTime;
    private Long endCreateTime;
    private Integer channel;
    private List<Integer> stateList;
    private String parkingNumber;
    private String parkingPhone;
    private String plateNumber;
    private String serialNumber;
    private String parklotName;
    private Integer parklotType;
    private Integer id;
}
