package com.qhieco.response.data.web;

import com.qhieco.commonentity.ActivityRule;
import com.qhieco.commonentity.relational.ActivityTagB;
import com.qhieco.constant.Status;
import lombok.Data;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/4/14 11:34
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class ActivityDetailData {
    private Integer id;
    private String name;
    private Integer type;
    private String fileLongPath;
    private String fileWidePath;
    private String intro;
    private String href;
    private Long beginTime;
    private Long endTime;
    private Integer fileLongId;
    private Integer fileWideId;
    private List<ActivityRule> activityRules;
    private List<ActivityTagB> activityTagBs;

}
