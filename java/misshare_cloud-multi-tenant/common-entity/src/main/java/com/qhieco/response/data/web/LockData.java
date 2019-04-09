package com.qhieco.response.data.web;

import com.qhieco.commonentity.iotdevice.Lock;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-19 下午5:46
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class LockData{
    private Integer invalidNum;
    private Integer noPowerNum;

    private Integer sEcho;
    private Integer iTotalRecords;
    private Integer iTotalDisplayRecords;
    private List<Lock> dataList;
}
