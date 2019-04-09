package com.qhieco.time;

import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/28 12:17
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ParklotIdAdParklocIds {

    private Integer parklotId;

    private List<ParklocInfo> parklocInfos;
}
