package com.qhieco.barrier.keytop.response;

import lombok.Data;

import java.util.List;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/5/22 17:18
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class KeyTopValidateParkingLotInfo {
    private String Status;
    private String Message;
    private List<String> Data;
}
