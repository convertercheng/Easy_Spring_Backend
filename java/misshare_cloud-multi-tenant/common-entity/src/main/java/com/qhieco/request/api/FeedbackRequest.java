package com.qhieco.request.api;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/1 15:56
 * <p>
 * 类说明：
 * 意见反馈请求参数
 */
@Data
public class FeedbackRequest extends AbstractRequest{
    private Integer user_id;
    private Integer parklot_id;
    private String remark;
    private Integer[] problem_ids;
    /**
     * 小程序不能传输整型数组，用此字段代替，id间用逗号,分割
     */
    private String problem_ids_str;
    private List<MultipartFile> file;
}
