package com.qhieco.response.data.web;

import com.qhieco.constant.Constants;
import com.qhieco.constant.Status;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/13 14:55
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class PrizeReceiveRecordData {

    private Integer id;
    private String prizeName;
    private String phone;
    private Integer number;
    private String activityName;
    private String triggerTypes;
    private Long createTime;

    private String triggerTypeStr;

    public String getTriggerTypeStr() {
        String str = "";
        if (!StringUtils.isEmpty(this.triggerTypes)) {
            String[] arr = this.triggerTypes.split(Constants.DELIMITER_COMMA);
            for (String s : arr) {
                try {
                    str += Status.TriggerType.find(Integer.valueOf(s)) + ";";
                } catch (Exception e) {
                }
            }
        }
        return str;
    }
}
