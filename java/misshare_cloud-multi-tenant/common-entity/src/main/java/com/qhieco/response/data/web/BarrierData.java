package com.qhieco.response.data.web;

import com.qhieco.commonentity.iotdevice.Barrier;
import lombok.Data;

/**
 * @author 王宇 623619462@qq.com
 * @version 2.0.1 创建时间: 18-3-9 下午3:37
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class BarrierData extends Barrier {

    private String parklotName;

    @Override
    public String toString() {
        return super.toString()+"{parklotName="+parklotName+"}";
    }
}
