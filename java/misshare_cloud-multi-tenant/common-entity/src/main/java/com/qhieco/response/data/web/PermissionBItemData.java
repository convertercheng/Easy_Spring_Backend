package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;

/**
 * @author 徐文敏
 * @version 2.0.1 创建时间: 2018/3/31 9:55
 * <p>
 * 类说明：
 * ${description}
 */
@Data
public class PermissionBItemData {

    private Integer id;

    private Integer state;

    private String stateStr;

    private Integer pid;

    private String name;

    private String url;

    private Long createTime;


    public String getStateStr() {
        return Status.Common.find(this.state);
    }

}
