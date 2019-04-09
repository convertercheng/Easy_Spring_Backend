package com.qhieco.apiread.web;

import com.qhieco.apiservice.AreaService;
import com.qhieco.apiservice.exception.ParamException;
import com.qhieco.constant.Status;
import com.qhieco.request.api.AreaGetRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.api.AreaGetRepData;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 赵翔 xiangflight@foxmail.com
 * @version 2.0.1 创建时间: 2018/3/6 下午8:46
 * <p>
 * 类说明：
 * 有关地区的一些操作
 */
@RestController
@RequestMapping("area")
@Slf4j
public class AreaWeb {

    @Autowired
    private AreaService areaService;

    @PostMapping("parklot/support")
    public Resp getSupportAreas(@RequestBody AreaGetRequest areaGetRequest) {
        Resp resp = ParamCheck.check(areaGetRequest, "user_id");
        if (!resp.getError_code().equals(Status.Common.VALID.getInt())) {
            throw new ParamException(resp, Status.ApiErr.PARAMS_AREAS_SUPPORT_GET.getCode());
        }
        List<AreaGetRepData> supportAreas = areaService.getSupportAreas(areaGetRequest);
        return RespUtil.successResp(supportAreas);
    }
}
