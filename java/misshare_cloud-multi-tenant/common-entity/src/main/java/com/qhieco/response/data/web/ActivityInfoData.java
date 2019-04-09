package com.qhieco.response.data.web;

import com.qhieco.constant.Status;
import lombok.Data;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:34
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivityInfoData {
    private Integer id;
    private String name;
    private Integer type;
    private String intro;
    private String href;
    private Long beginTime;
    private Long endTime;
    private Integer state;
    private Long createTime;
    private Integer browseCount;
    private Integer participateCount;
    private Integer prizeCount;

    private String stateStr;
    private String typeStr;
    public String getStateStr() {
        if(this.state.equals(Status.Common.INVALID.getInt())){
            return "已停止";
        }
        if(this.state.equals(Status.Common.VALID.getInt())){
            Long now = System.currentTimeMillis();
            if(this.beginTime <= now && now <= this.endTime){
                return "进行中";
            }else if(this.beginTime > now){
                return "待开始";
            }else if(now > endTime){
                return "已过期";
            }
        }
        return Status.Common.find(this.state);
    }
    public String getTypeStr(){
        return Status.ActivityType.find(this.type);
    }

}
