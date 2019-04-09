package com.qhieco.response.data.api;

import lombok.Data;

/**
 * @author  liujiangjiang
 * Created by liujiangjiang on 17/9/29.
 */
@Data
public class NBLockData {
    private String macid;

    private String status;

    private String FW_Version;

    private String enable;

    private String VBat;

    private String msg_id;

    private String time;

    private String type;
}
