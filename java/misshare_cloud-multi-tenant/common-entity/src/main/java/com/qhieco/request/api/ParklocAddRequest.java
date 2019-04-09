package com.qhieco.request.api;

import lombok.Data;

/**
 * @author 刘江茳 363834586@qq.com
 * @version 2.0.1 创建时间: 2018/3/6 10:39
 * <p>
 * 类说明：
 *       添加车位请求类
 */
@Data
public class ParklocAddRequest extends AbstractRequest {



    private Integer user_id;

    private Integer area_id;

    private String parklot_name;

    private String contact_phone;

}
