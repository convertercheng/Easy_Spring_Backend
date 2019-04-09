package com.qhieco.web.controller;

import com.qhieco.commonentity.Area;
import com.qhieco.constant.Status;
import com.qhieco.request.web.AreaRequest;
import com.qhieco.response.Resp;
import com.qhieco.response.data.web.AreaData;
import com.qhieco.util.ParamCheck;
import com.qhieco.util.RespUtil;
import com.qhieco.webservice.AreaService;
import com.qhieco.webservice.exception.ParamException;
import com.qhieco.webservice.exception.QhieWebException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 许家钰 xujiayu0837@163.com
 * @version 2.0.1 创建时间: 18/3/9 上午11:39
 *          <p>
 *          类说明：
 *          地区模块接口类
 */
@RestController
@RequestMapping("area")
@Slf4j
public class AreaWeb {
    @Autowired
    private AreaService areaService;

    @PostMapping(value = "pageable")
    public Resp children(AreaRequest areaRequest) {
        Resp checkResp = ParamCheck.check(areaRequest, "sEcho", "start", "length");
        if (!checkResp.getError_code().equals(Status.Common.VALID.getInt())){
            throw new ParamException(checkResp, Status.WebErr.SYSTEM_ERROR.getCode());
        }
        return areaService.children(areaRequest);
    }

    @PostMapping("save")
    public Resp save(@ModelAttribute Area area) {
        Resp resp = areaService.save(area);
        Object obj = resp.getData();
        if(obj instanceof Area) {
            Area entity = (Area) obj;
            log.info("Area with id " + entity.getId() + " saved successfully.");
            return RespUtil.successResp();
        } else {
            return RespUtil.errorResp(resp.getError_code(), resp.getError_message());
        }
    }

    @GetMapping("/{parentId}")
    public Resp getAreas(@PathVariable Integer parentId) {
        if (null == parentId) {
            throw new QhieWebException(Status.WebErr.EMPTY_AREA_GET_PARAM);
        }
        List<AreaData> areas = areaService.getAreas(parentId);
        return RespUtil.successResp(areas);
    }

}
