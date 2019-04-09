package com.qhieco.request.web;

import com.qhieco.commonentity.ActivityRule;
import com.qhieco.commonentity.relational.ActivityTagB;
import com.qhieco.request.api.AbstractRequest;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 添加或修改活动请求参数
 */
@Data
public class ActivitySaveRequest extends AbstractRequest {
    private Integer id;
    private String name;
    private String intro;
    private Long beginTime;
    private Long endTime;
    private Integer type;
    private Integer state;
    private String href;
    private MultipartFile fileLong;
    private MultipartFile fileWide;
    private Integer fileLongId;
    private Integer fileWideId;

    private List<ActivityRule> activityRules;
    private List<ActivityTagB> activityTagBs;

}
