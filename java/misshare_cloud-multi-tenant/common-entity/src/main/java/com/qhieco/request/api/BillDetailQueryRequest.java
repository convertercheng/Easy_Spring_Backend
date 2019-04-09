package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/2 16:42
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class BillDetailQueryRequest extends AbstractRequest {
    private String serial_number;
    /**
     * 账单类型0:预约订单，1:停车订单，2:退款订单，3:充值订单，4:提现订单 5 车位收入 6: 预约收费。业主、物业的预约费收入 7: 停车收费 (从列表的数据直接取得)
     */
    private Integer type;

    private Integer user_id;
}
