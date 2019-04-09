package com.qhieco.response.data.web;

import com.qhieco.request.web.QueryPaged;
import lombok.Data;

import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/4/7 11:55
 * <p>
 * 类说明：
 * ${说明}
 */
@Data
public class FeedBackData extends QueryPaged {
    /**
     * 反馈意见ID
     */
    private Integer feedbackId;
    /**
     * 用户账号
     */
    private String phone;
    /**
     * 反馈时间
     */
    private long createTime;
    /**
     * 来源
     */
    private String resource;
    /**
     * 反馈意见
     */
    private String proIntro;
    /**
     * 补充说明
     */
    private String remark;
    /**
     * 文件ids
     */
    private String fileIds;
    /**
     * 图片
     */
    private List<String> filePaths;
}
