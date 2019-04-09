package com.qhieco.response.data.api;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/3/30 10:36
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParklocLockAccessListRespData {
    private List<ParklocLockRespData> lockList;
    private List<AccessRespData> accessList;
}
