package com.qhieco.request.web;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 蒙延章 970915683@qq.com
 * @version 2.0.1 创建时间: 2018/6/12 15:25
 * <p>
 * 类说明：
 * 添加奖品参数
 */
@Data
public class PrizeEntityRequest {
    private Integer id;
    private String name;
    private Integer type;
    private BigDecimal money;
    private String intro;
    /**
     * 首次保存的时候此字段为空，后面更新必须要带上此参数
     */
    private Integer fileId;
    /**
     * 更新图片时必须要带上此参数
     */
    private List<MultipartFile> files;
    private Long startTime;
    private Long endTime;
}
