package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;

/**
 * @author 黄金芽 327357731@qq.com
 * @version 2.0.1 创建时间: 2018/3/29 9:18
 * <p>
 * 类说明：
 * 优惠券返回结果类
 */
@Data
public class LogOperationWebData {
    private Integer webUserId;

    private String webUserName;

    private String content;

    private String sourceIp;

    private Long operateTime;

    private String stateStr;

    private Integer state;

    public String getStateStr() {
        String desc = "";
        if (state != null) {
            desc = Status.Common.find(this.state);
        }
        return desc;
    }

}
