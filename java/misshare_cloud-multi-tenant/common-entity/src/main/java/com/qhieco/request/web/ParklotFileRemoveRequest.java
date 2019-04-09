package com.qhieco.request.web;

import lombok.Data;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/4/16 下午9:29
 * <p>
 * 类说明：
 *     停车场删除图片请求
 */
@Data
public class ParklotFileRemoveRequest {

    private Integer parklotId;

    private Integer fileId;

}
